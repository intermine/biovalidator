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

import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.validator.RuleValidator;
import org.intermine.biovalidator.validator.gff3.FeatureLine;

import java.util.Set;

/**
 * Rule Validator for validating feature-types(column 3) against SequenceOntology terms
 * @author deepak
 */
public class GFF3FeatureTypeRulValidator implements RuleValidator<FeatureLine>
{
    private Set<String> sequenceOntologyFeatureTypes;

    /**
     * Construct a GFF3 feature type validator(for column 3) with a set of
     * Sequence-Ontology terms
     * @param sequenceOntologyFeatureTypes set of all SO terms
     */
    public GFF3FeatureTypeRulValidator(Set<String> sequenceOntologyFeatureTypes) {
        this.sequenceOntologyFeatureTypes = sequenceOntologyFeatureTypes;
    }

    @Override
    public boolean validateAndAddError(FeatureLine feature, ValidationResult validationResult,
                                       long currentLineNum) {
        if (!sequenceOntologyFeatureTypes.contains(feature.getType())) {
            String errMessage = "unknown feature type '" + feature.getType()
                    + "' at line " + currentLineNum;
            validationResult.addError(errMessage);
            return false;
        }
        return true;
    }
}
