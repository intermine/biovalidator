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

import org.apache.commons.lang3.math.NumberUtils;
import org.intermine.biovalidator.api.DefaultValidationResult;
import org.intermine.biovalidator.api.ErrorMessage;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.strategy.ValidationResultStrategy;
import org.intermine.biovalidator.validator.RuleValidator;
import org.intermine.biovalidator.validator.gff3.FeatureLine;
import org.intermine.biovalidator.validator.gff3.SequenceRegion;

import java.util.Map;

/**
 * Validates 'start' and 'end' coordinate(columns 4 and 5) of a feature
 * @author deepak
 */
public class GFF3StartAndEndCoordinateRuleValidator implements RuleValidator<FeatureLine>
{
    private static final String SEQUENCE_REGION_DIRECTIVE = "##sequence-region";

    private Map<String, SequenceRegion> sequenceRegionDirectives;

    /**
     * Construct a GFF3 'start' and 'end' coordinate validator with the mapping of all the
     * ##sequence region directives of the input file
     * @param seqRegionDirectives mapping of all #sequence-region directive
     *                            each entry will contain seqId as the key and
     *                            SequenceRegion instance as value(representing both'start'
     *                            and 'end coordinates of the region')
     */
    public GFF3StartAndEndCoordinateRuleValidator(Map<String, SequenceRegion> seqRegionDirectives) {
        this.sequenceRegionDirectives = seqRegionDirectives;
    }

    @Override
    public boolean validateAndAddError(FeatureLine feature, ValidationResult validationResult,
                                       long currentLineNum) {
        ValidationResultStrategy resultStrategy = ((DefaultValidationResult) validationResult)
                .getResultStrategy();

        //Check whether 'start' and 'end' are numbers or not
        if (!NumberUtils.isParsable(feature.getStartCord())) {
            String invalidStartCordMsg = "start coordinate value '" + feature.getStartCord()
                    + "' is not a number at line " + currentLineNum;
            validationResult.addError(ErrorMessage.of(invalidStartCordMsg));

            /*
              return even if resultStrategy.shouldStopAtFirstError() is true, because if start
              or end coordinates are not numbers then other validation cannot be performed if
              those validation depends on parsing start and end cord
            */
            return false;
        }
        if (!NumberUtils.isParsable(feature.getEndCord())) {
            String invalidStartCordMsg = "end coordinate value '" + feature.getEndCord()
                    + "' is not a number at line " + currentLineNum;
            validationResult.addError(ErrorMessage.of(invalidStartCordMsg));
            return false;
        }

        // proceed to other validation only if start and end is parsable
        long startCord = Long.parseLong(feature.getStartCord());
        long endCord = Long.parseLong(feature.getEndCord());

        //Check 'start' is smaller or equals to 'end' and visa-versa
        //'start' and 'end' can't be zero and end should be greater than start
        if (startCord < 1 || endCord < 1 || endCord < startCord) {
            String coordinateErrMsg = "Start must be greater than zero and"
                    + " less or equal to end coordinate at line " + currentLineNum;
            validationResult.addError(ErrorMessage.of(coordinateErrMsg));
            if (resultStrategy.shouldStopAtFirstError()) {
                return false;
            }
        }

        return validateStartAndEndCoordinateWithinSequenceRegionBounds(
                feature, startCord, endCord, validationResult, currentLineNum);
    }

    /**
     * if ##sequence-region directive is defined for the current seqId then check
     * start and end coordinate of current feature is within the range of
     * defined ##sequence-region directive or not
     */
    private boolean validateStartAndEndCoordinateWithinSequenceRegionBounds(FeatureLine feature,
            long startCord, long endCord, ValidationResult validationResult, long currentLineNum) {
        String seqId = feature.getSeqId();
        Map<String, String> featureAttributes = feature.getAttributesMapping();

        /*
            feature with Is_Circular attribute is allowed to cross bounds of ##sequence-region
            directive so, don't check this rule circular features
         */
        if (featureAttributes.containsKey("Is_circular")) {
            boolean isCircularValueTrue = Boolean.parseBoolean(
                    featureAttributes.get("Is_circular"));
            if (isCircularValueTrue) {
                return true; // if a feature is circular then ignore ##sequence-region range check
            }
        }
        if (sequenceRegionDirectives.containsKey(seqId)) {
            SequenceRegion seqRegion = sequenceRegionDirectives.get(seqId);
            if (startCord < seqRegion.getSequenceRegionStart()) {
                String msg = "start coordinate of seqId '" + seqId + "' is not within the range"
                        + "of " + SEQUENCE_REGION_DIRECTIVE + " at line " + currentLineNum
                        + ", it must be greater or equals " + seqRegion.getSequenceRegionStart();
                validationResult.addError(msg);
                return false;
            } else if (endCord > seqRegion.getSequenceRegionEnd()) {
                String msg = "end coordinate of seqId '" + seqId + "' is not within the range"
                        + "of " + SEQUENCE_REGION_DIRECTIVE + " at line " + currentLineNum
                        + ", it must be less or equals " + seqRegion.getSequenceRegionEnd();
                validationResult.addError(msg);
                return false;
            }
        }
        return true;
    }
}
