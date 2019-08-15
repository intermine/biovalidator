package org.intermine.biovalidator.validator.gff3.rulevalidator;

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
import org.intermine.biovalidator.api.DefaultValidationResult;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.strategy.ValidationResultStrategy;
import org.intermine.biovalidator.validator.RuleValidator;
import org.intermine.biovalidator.validator.gff3.FeatureLine;

/**
 * Validates score(column-6), strand(column-7) and phase(comun-8)
 * of a feature.
 * @author deepak
 */
public class GFF3ScoreStrandAndPhaseRuleValidator implements RuleValidator<FeatureLine>
{
    private static final String CDS = "CDS";

    @Override
    public boolean validateAndAddError(FeatureLine feature, ValidationResult validationResult,
                                       long currentLineNum) {
        ValidationResultStrategy validationResultStrategy =
                ((DefaultValidationResult) validationResult).getResultStrategy();

        // validates score value
        String score = feature.getScore();
        if (!isValidScoreValue(score)) {
            validationResult.addError("score value must be a floating point number or '.',"
                    + " but found '" + score + "' at line " + currentLineNum);
            if (validationResultStrategy.shouldStopAtFirstError()) {
                return false;
            }
        }

        // validate strand value
        String strand = feature.getStrand();
        if (!isValidStrandValue(strand)) { //checks strand value is valid
            validationResult.addError("strand value must be one of ('-', '+', '?') but found '"
                    + strand + "' at line " + currentLineNum);
            if (validationResultStrategy.shouldStopAtFirstError()) {
                return false;
            }
        }

        //validate phase value
        String phase = feature.getPhase();
        if (!isValidPhaseValue(phase)) {
            validationResult.addError("phase value can only be one of 0, 1, 2 or '.', but found '"
                    + phase + "' at line " + currentLineNum);
            if (validationResultStrategy.shouldStopAtFirstError()) {
                return false;
            }
        }
        if (!isValidCDSPhaseValue(feature.getType(), phase)) {
            validationResult.addError("phase is required for CDS and can only be 0, 1 or 2 at line "
                    + currentLineNum);
            if (validationResultStrategy.shouldStopAtFirstError()) {
                return false;
            }
        }
        return true; // if no rules violated
    }

    private boolean isValidStrandValue(String strand) {
        return StringUtils.equalsAny(strand, ".", "-", "+", "?");
    }

    private boolean isValidScoreValue(String score) {
        return ".".equals(score) || NumberUtils.isCreatable(score);
    }

    private boolean isValidPhaseValue(String phase) {
        if (".".equals(phase)) {
            return true;
        }
        if (isInteger(phase)) {
            int phaseVal = Integer.parseInt(phase);
            return phaseVal >= 0 && phaseVal < 3;
        }
        return false;
    }

    private boolean isValidCDSPhaseValue(String featureType, String phase) {
        if (CDS.equalsIgnoreCase(featureType) ) {
            return StringUtils.equalsAny(phase, "0", "1", "2");
        }
        return true;
    }

    /**
     * checks whether string is number or not
     * @param phase value to be tested
     * @return true or false
     */
    private boolean isInteger(String phase) {
        return phase.matches("[0-9]{1,9}"); //TODO need to be refactored
    }
}
