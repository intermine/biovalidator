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

import java.util.regex.Pattern;

/**
 * GFF3 seqId(column-1) rule validator:
 * Check whether seqId is valid or not.
 * Rules:
 * 1. valid pattern for seqId is one or more of 'a-zA-Z0-9.:^*$@!+_?-|'
 * 2. escaped '>' and space(' ') are allowed
 *
 * @author deepak
 */
public class GFF3SeqIdRuleValidator implements RuleValidator<FeatureLine>
{
    private static final Pattern SEQUENCE_ID_VALID_PATTERN =
            Pattern.compile("[a-zA-Z0-9.:^*$@!+?-|\\-]+");

    @Override
    public boolean validateAndAddError(FeatureLine feature, ValidationResult validationResult,
                                       long currentLineNum) {
        String seqId = feature.getSeqId();

        //replace all the escaped '>' and spaces as these two are valid
        seqId = StringUtils.replace(seqId, "\\>", "");
        seqId = StringUtils.replace(seqId, "\\ ", "");

        if (SEQUENCE_ID_VALID_PATTERN.matcher(seqId).matches()) {
            return true;
        } else {
            validationResult.addError("Invalid Sequence Id '"
                    + feature.getSeqId() + "' at line " + currentLineNum);
            return false;
        }
    }
}
