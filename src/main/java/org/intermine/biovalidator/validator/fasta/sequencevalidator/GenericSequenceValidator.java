package org.intermine.biovalidator.validator.fasta.sequencevalidator;

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
import org.intermine.biovalidator.api.ValidationResult;

import javax.annotation.Nonnull;

public class GenericSequenceValidator implements SequenceValidator
{
    @Override
    public boolean validate(@Nonnull String sequence,
                            long seqLineNo,
                            @Nonnull ValidationResult result) {
        if (seqLineNo == 21968) {
            seqLineNo = seqLineNo;
        }
        for (int i=0; i<sequence.length(); i++) {
            if (!isValidLetter(sequence.charAt(i))) {
                String msg = "Invalid letter '" + sequence.charAt(i)
                        + "' at line " + seqLineNo + " and col " + (+1);
                        result.getErrorMessages().add(new ErrorMessage(msg));
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isValid(@Nonnull String sequence) {
        return false;
    }

    @Override
    public boolean isValidLetter(char c) {
        return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '-' || c == '*');
    }
}
