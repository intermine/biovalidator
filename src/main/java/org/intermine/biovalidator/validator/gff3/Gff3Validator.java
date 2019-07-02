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

import org.intermine.biovalidator.api.ErrorMessage;
import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.parser.Gff3FeatureParser;
import org.intermine.biovalidator.validator.AbstractValidator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author deepak
 */
public class Gff3Validator extends AbstractValidator
{
    private static final String SEQUENCE_ID_VALID_PATTERN = "[a-zA-Z0-9.:^*$@!+_?-|]+";
    private static final String FASTA_DIRECTIVE = "##FASTA";
    private static final String GFF3_HEADER = "##gff-version 3";

    private InputStreamReader inputStreamReader;

    private Set<String> uniqueIdAttributesSet;

    /**
     * Contruct a Gff3Validator with an input stream
     * @param inputStreamReader input source
     */
    public Gff3Validator(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
        this.uniqueIdAttributesSet = new HashSet<>();
    }

    @Nonnull
    @Override
    public ValidationResult validate() throws ValidationFailureException {
        try (Parser<Optional<Gff3Line>> parser = new Gff3FeatureParser(inputStreamReader)) {
            long currentLineNum = 1;
            Optional<Gff3Line> lineOpt = parser.parseNext();

            if (lineOpt.isPresent() && !isGff3HeaderLine(lineOpt.get())) {
                validationResult.addError(ErrorMessage.of("Invalid Gff file"));
            }

            while (lineOpt.isPresent()) {
                Gff3Line line = lineOpt.get();

                if (line instanceof Gff3CommentLine) { //if line a comment line
                    Gff3CommentLine comment = (Gff3CommentLine) line;
                    if (comment.getComment().startsWith(FASTA_DIRECTIVE)) { //End of GFF3 content
                        return validationResult;
                    }
                } else {
                    FeatureLine feature = (FeatureLine) line;
                    validateFeature(feature, currentLineNum);
                }

                if (!validationResult.isValid()) {
                    return validationResult;
                }
                lineOpt = parser.parseNext(); //next featureLine
                currentLineNum++;
            }
            return validationResult;
        } catch (IOException e) {
            throw new ValidationFailureException(e.getMessage());
        }
    }

    private boolean isGff3HeaderLine(Gff3Line line) {
        if (line instanceof Gff3CommentLine) {
            Gff3CommentLine comment = (Gff3CommentLine) line;
            return comment.getComment().startsWith(GFF3_HEADER);
        }
        return false;
    }

    private void validateFeature(FeatureLine feature, long currentLineNum) {
        String seqId = feature.getSeqId();
        if (!seqId.matches(SEQUENCE_ID_VALID_PATTERN)) {
            validationResult.addError(ErrorMessage.of("Invalid Sequence Id at " + currentLineNum));
        }

        if (feature.getStartCord() > feature.getEndCord()) {
            String coordinateErrMsg = "Start coordinate must be greater or equal to end"
                + " coordinate at line " + currentLineNum;
            validationResult.addError(ErrorMessage.of(coordinateErrMsg));
        }

        String score = feature.getScore();

        Map<String, String> keyValPairAttributes = feature.getAttributesMapping();

        //check unique ID attributes
        if (keyValPairAttributes.containsKey("ID")) {
            String idValue = keyValPairAttributes.get("ID");
            if (uniqueIdAttributesSet.contains(idValue)) {
                String duplicateErrMsg = "Duplicate ID at line " + currentLineNum;
                validationResult.addError(ErrorMessage.of(duplicateErrMsg));
            } else {
                uniqueIdAttributesSet.add(idValue);
            }
        }

        //check parent exist if attribute has parent value
        if (keyValPairAttributes.containsKey("Parent")) {
            if (!uniqueIdAttributesSet.contains("Parent")) {
                String parentErrMsg = "Parent no found at line " + currentLineNum;
                validationResult.addError(ErrorMessage.of(parentErrMsg));
            }
        }
    }
}
