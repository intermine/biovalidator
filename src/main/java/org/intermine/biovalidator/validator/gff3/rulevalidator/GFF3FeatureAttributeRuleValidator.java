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
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.validator.RuleValidator;
import org.intermine.biovalidator.validator.gff3.FeatureLine;

import java.util.Map;
import java.util.Set;

/**
 * Validator for validating GFF3 feature attributes(column-3)
 * - validates key and value of the attribute column of a feature
 * @author deepak
 */
public class GFF3FeatureAttributeRuleValidator implements RuleValidator<FeatureLine>
{
    private Set<String> uniqueIdAttributesSet;
    private Set<String> uniqueNameAttributeSet;

    /**
     * Construct a GFF3FeatureAttributeRuleValidator with with uniqueIdAttributesSet
     * and uniqueNameAttributeSet.
     * @param uniqueIdAttributesSet set of ID attribute tag value
     * @param uniqueNameAttributeSet set of Name attribute tag value
     */
    public GFF3FeatureAttributeRuleValidator(Set<String> uniqueIdAttributesSet,
                                             Set<String> uniqueNameAttributeSet) {
        this.uniqueIdAttributesSet = uniqueIdAttributesSet;
        this.uniqueNameAttributeSet = uniqueNameAttributeSet;
    }
    @Override
    public boolean validateAndAddError(FeatureLine feature, ValidationResult validationResult,
                                       long currentLineNum) {
        //check attribute key and value is empty or not
        Map<String, String> attributesMapping = feature.getAttributesMapping();
        //Set<String> uniqueKeys = new HashSet<>();
        for (Map.Entry<String, String> entry: attributesMapping.entrySet()) {
            String attrTag = entry.getKey();
            String attrVal = entry.getValue();

            if (StringUtils.isBlank(attrTag)) {
                validationResult.addWarning("attribute tag's key is missing or empty for value '"
                        + attrVal + "' at line " + currentLineNum);
            }
            if (StringUtils.isBlank(attrVal)) {
                validationResult.addWarning("attribute value is missing or empty for key '"
                        + attrTag + "' at line " + currentLineNum);
            }
        }

        //If feature has a parent key then check its parent exist or not
        Map<String, String> keyValPairAttributes = feature.getAttributesMapping();
        if (keyValPairAttributes.containsKey("Parent")) {
            String parentVal = keyValPairAttributes.get("Parent");
            if (!uniqueIdAttributesSet.contains(parentVal)
                    && !uniqueNameAttributeSet.contains(parentVal)) {
                String errMsg = "Parent '" + parentVal + "' not found at line " + currentLineNum;
                validationResult.addError(errMsg);
                return false;
            }
        }
        return true;
    }

}
