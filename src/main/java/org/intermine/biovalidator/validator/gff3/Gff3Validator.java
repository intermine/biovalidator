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
import java.util.Optional;

/**
 * @author deepak
 */
public class Gff3Validator extends AbstractValidator
{
    private static final String SEQUENCE_ID_VALID_PATTERN = "[a-zA-Z0-9.:^*$@!+_?-|]+";
    private InputStreamReader inputStreamReader;

    /**
     * Contruct a Gff3Validator with an input stream
     * @param inputStreamReader input source
     */
    public Gff3Validator(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
    }

    @Nonnull
    @Override
    public ValidationResult validate() throws ValidationFailureException {
        try (Parser<Optional<Feature>> parser = new Gff3FeatureParser(inputStreamReader)) {
            long currentLineNum = 1;

            Optional<Feature> featureOpt = parser.parseNext();

            while (featureOpt.isPresent()) {
                Feature feature = featureOpt.get();
                validateFeature(feature, currentLineNum);
                featureOpt = parser.parseNext(); //next feature
            }
            return validationResult;
        } catch (IOException e) {
            throw new ValidationFailureException(e.getMessage());
        }
    }

    private void validateFeature(Feature feature, long currentLineNum) {
        String seqId = feature.getSeqId();
        if (!seqId.matches(SEQUENCE_ID_VALID_PATTERN)) {
            validationResult.addError(ErrorMessage.of("Invalid Sequence Id at " + currentLineNum));
        }

        if (feature.getStartCord() > feature.getEndCord()) {
            String coordinateErrMsg = "Start coordinate must be greater or equal to end"
                + " coordinate at line " + currentLineNum;
            validationResult.addError(ErrorMessage.of(coordinateErrMsg));
        }

        //String score = feature.getScore();

    }
}
