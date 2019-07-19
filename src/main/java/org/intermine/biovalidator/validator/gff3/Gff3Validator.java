package org.intermine.biovalidator.validator.gff3;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.intermine.biovalidator.api.ErrorMessage;
import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.WarningMessage;
import org.intermine.biovalidator.parser.Gff3FeatureParser;
import org.intermine.biovalidator.validator.AbstractValidator;
import org.intermine.biovalidator.validator.RuleValidator;
import org.intermine.biovalidator.validator.gff3.rulevalidator.GFF3FeatureAttributeRuleValidator;
import org.intermine.biovalidator.validator.gff3.rulevalidator.GFF3FeatureTypeRulValidator;
import org.intermine.biovalidator.validator.gff3.rulevalidator.GFF3ScoreStrandAndPhaseRuleValidator;
import org.intermine.biovalidator.validator.gff3.rulevalidator.GFF3SeqIdRuleValidator;
import org.intermine.biovalidator.validator.gff3.rulevalidator.GFF3StartAndEndCoordinateRulValidator;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author deepak
 */
public class Gff3Validator extends AbstractValidator
{
    private static final String PROCESSED_SO_TERMS_FILENAME = "/gff3/processed_so_terms.obo";
    private static final String SEQUENCE_REGION_DIRECTIVE = "##sequence-region";
    private static final String FASTA_DIRECTIVE = "##FASTA";
    private static final String GFF3_HEADER = "##gff-version";
    private static final int GFF_VERSION = 3;

    private static final Pattern VERSION_NUMBER_PATTERN =
            Pattern.compile("(\\d+\\.)?(\\d+\\.)?(\\*|\\d+)");

    private InputStreamReader inputStreamReader;

    private Set<String> sequenceOntologyFeatureTypes;
    private Set<String> uniqueIdAttributesSet;
    private Set<String> uniqueNameAttributeSet;

    private Map<String, SequenceRegion> sequenceRegionDirectives;

    private List<RuleValidator<FeatureLine>> gff3RuleValidator;

    /**
     * Contruct a Gff3Validator with an input stream
     * @param inputStreamReader input source
     */
    public Gff3Validator(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
        this.uniqueIdAttributesSet = new HashSet<>();
        this.uniqueNameAttributeSet = new HashSet<>();

        //store start/end coordinate all the ##sequence-region directives available in the file
        this.sequenceRegionDirectives = new HashMap<>();

        /*
         parse SequenceOntology terms from file 'processed_so_terms.obo'
         and store all the SO-Terms in a Set
        */
        try {
            this.sequenceOntologyFeatureTypes =
                    parseSequenceOntologyTypes(PROCESSED_SO_TERMS_FILENAME);
        } catch (IOException e) {
            throw new IllegalArgumentException("unable to parse sequence-ontology term file");
        }
        this.gff3RuleValidator = createGff3RuleValidator();
    }

    @Nonnull
    @Override
    public ValidationResult validate() {
        try (Parser<Optional<Gff3Line>> parser = new Gff3FeatureParser(inputStreamReader)) {
            long currentLineNum = 1;
            Optional<Gff3Line> lineOpt = parser.parseNext();

            // first line must be a valid gff3 header line
            if (!lineOpt.isPresent() || !isValidGff3HeaderLine(lineOpt.get())) {
                String msg = "Invalid Gff file! first line must be a header line"
                        + " and version version must be in the format 3.#.#";
                validationResult.addError(ErrorMessage.of(msg));
            }

            while (lineOpt.isPresent()) {
                Gff3Line line = lineOpt.get();

                if (line instanceof Gff3DirectiveLine) { //if line a directive line
                    Gff3DirectiveLine directive = (Gff3DirectiveLine) line;
                    if (directive.getComment().startsWith(FASTA_DIRECTIVE)) { //End of GFF3 content
                        return validationResult;
                    }
                    processDirectiveLine(directive, currentLineNum);
                } else {
                    FeatureLine feature = (FeatureLine) line;
                    validateFeature(feature, currentLineNum);
                }

                if (!validationResult.isValid()
                        && validationResultStrategy.shouldStopAtFirstError()) {
                    return validationResult;
                }
                lineOpt = parser.parseNext(); //next featureLine
                currentLineNum++;
            }
            return validationResult;
        } catch (IOException e) {
            addError(e.getMessage());
            return validationResult;
        }
    }

    /**
     * Create and returns a list of GFF3 rule validators which can be used to validate
     * a particular feature by iterating over this list and calling
     * validateAndAddError() method
     * @return list of GFF3 rule validators
     */
    private List<RuleValidator<FeatureLine>> createGff3RuleValidator() {
        return Arrays.asList(
              new GFF3SeqIdRuleValidator(),
              new GFF3FeatureTypeRulValidator(sequenceOntologyFeatureTypes),
              new GFF3StartAndEndCoordinateRulValidator(sequenceRegionDirectives),
              new GFF3ScoreStrandAndPhaseRuleValidator(),
              new GFF3FeatureAttributeRuleValidator(uniqueIdAttributesSet, uniqueNameAttributeSet)
        );
    }

    private void validateFeature(FeatureLine feature, long currentLineNum) {
        //iterate over each GFF3 rule validators
        for (RuleValidator<FeatureLine> ruleValidator: gff3RuleValidator) {
            boolean isValid = ruleValidator.validateAndAddError(feature,
                    validationResult, currentLineNum);

            if (!isValid && validationResultStrategy.shouldStopAtFirstError()) {
                return;
            }
        }
        //add attribute 'ID' uniqueIdAttributesSet and 'Name' to uniqueNameAttributeSet if exist
        Map<String, String> keyValPairAttributes = feature.getAttributesMapping();
        if (keyValPairAttributes.containsKey("ID")) {
            uniqueIdAttributesSet.add(keyValPairAttributes.get("ID"));
        }
        if (keyValPairAttributes.containsKey("Name")) {
            uniqueNameAttributeSet.add(keyValPairAttributes.get("Name"));
        }
    }

    /**
     * Test whether a ##gff-version header is correct
     * Rules:
     *      1. must start with ##gff-version
     *      2. version must start with 3
     *      3. version itself must be a valid version number
     * @param line comment line
     * @return whether gff3 header is valid or not
     */
    private boolean isValidGff3HeaderLine(Gff3Line line) {
        if (line instanceof Gff3DirectiveLine) {
            Gff3DirectiveLine comment = (Gff3DirectiveLine) line;
            String headerLine = comment.getComment();
            if (!headerLine.startsWith(GFF3_HEADER)) {
                return false;
            }
            /*
                check gff3 version 3 is valid or not
                GFF3 version format: ##gff-version 3.#.#
             */
            String[] values = headerLine.split("\\s+");
            if (values.length < 2) {
                return false;
            }
            String version = values[1];
            int majorVersion = extractMajorPartOfVersion(version);
            if (majorVersion != GFF_VERSION || !VERSION_NUMBER_PATTERN.matcher(version).matches()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Process GFF3 directives, directive start with '##' in GFF3
     * @param directiveLine directive line instance
     * @param currentLineNum current line number
     */
    private void processDirectiveLine(Gff3DirectiveLine directiveLine, long currentLineNum) {
        String comment = directiveLine.getComment();

        // Process ##sequence-region directive
        if (comment.startsWith(SEQUENCE_REGION_DIRECTIVE)) {
            ImmutablePair<String, SequenceRegion> seqIdAndSequenceRegionPair =
                parseSequenceRegion(comment);
            if (!seqIdAndSequenceRegionPair.equals(ImmutablePair.nullPair())) { //if parsed
                String seqId =  seqIdAndSequenceRegionPair.getLeft();
                if (sequenceRegionDirectives.containsKey(seqId)) {
                    String msg = "Found more than one entry for "
                        + SEQUENCE_REGION_DIRECTIVE + " " + seqId + " at line " + currentLineNum;
                    addWarning(msg);
                } else {
                    sequenceRegionDirectives.put(seqId, seqIdAndSequenceRegionPair.getRight());
                }
            }
        }
    }

    /**
     * Parse ##sequence-region directive and returns a pair of seqId and SequenceRegion
     * Format of this directive is:
     *      ##sequence-region {@literal <seqId>} {@literal <start>} {@literal <end>}
     * @param comment comment to be parse
     * @return pair of seqId and SequenceRegion
     */
    private ImmutablePair<String, SequenceRegion> parseSequenceRegion(String comment) {
        if (StringUtils.isNoneBlank(comment)) {
            //TODO split only on raw space but not on escaped spaces
            String[] values = comment.split("\\s+"); //split by skipping empty spaces
            if (values.length >= 4) {
                String seqId = values[1];
                String start = values[2];
                String end = values[3];
                if (NumberUtils.isParsable(start) && NumberUtils.isParsable(end)) {
                    long startValue = Long.parseLong(start);
                    long endValue = Long.parseLong(end);
                    return ImmutablePair.of(seqId, SequenceRegion.of(startValue, endValue));
                }
            }
        }
        return ImmutablePair.nullPair();
    }

    /**
     * Extract major version part from a given version string
     * Example:
     *      '3.4.5'   ->   3
     *      '34.1.6'  ->   34
     *      'x.y.z'   ->   0
     * @param version version string in format 'X.X.X'
     * @return return major part of the version if exist or else return 0
     */
    private int extractMajorPartOfVersion(String version) {
        String majorVersion = StringUtils.substringBefore(version, ".");
        if (NumberUtils.isParsable(majorVersion)) {
            return Integer.parseInt(majorVersion);
        } else {
            return 0;
        }
    }

    /**
     * Parse SequenceOntology terms from file and store all the terms in to a set
     * @param filename filename from which SP terms to be parsed
     * @return set of parsed SO terms
     * @throws IOException if unable to parse
     */
    private Set<String> parseSequenceOntologyTypes(String filename) throws IOException {
        Set<String> featureTypes = new HashSet<>();
        try (InputStream is = getClass().getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (StringUtils.isNotBlank(line)) {
                    featureTypes.add(line.trim());
                }
            }
        }
        /*Files.lines(Paths.get(filename)).forEach(line -> {
            if (StringUtils.isNotBlank(line)) {
                featureTypes.add(line.trim());
            }
        });*/
        return featureTypes;
    }

    private void addError(String msg) {
        validationResult.addError(ErrorMessage.of(msg));
    }

    private void addWarning(String msg) {
        validationResult.addWarning(WarningMessage.of(msg));
    }
}
