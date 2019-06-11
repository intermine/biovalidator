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
import org.intermine.biovalidator.api.WarningMessage;

import javax.annotation.Nonnull;

public class GenericSequenceValidator implements SequenceValidator
{
    @Override
    public int validate(@Nonnull String sequence,
                            long seqLineNo,
                            @Nonnull ValidationResult result) {
        for (int i = 0; i < sequence.length(); i++) {
            if (!isValidLetter(sequence.charAt(i))) {
                String msg = "Invalid letter '" + sequence.charAt(i)
                        + "' at line " + seqLineNo + " and col " + (i + 1);
                result.addError(ErrorMessage.of(msg));
                return (i + 1);
            }
        }
        if (sequence.length() > 80) {
            result.addWarning(WarningMessage.of("number of sequence length exceed 80 at line "
                    + seqLineNo));
        }
        return sequence.length();
    }

    @Override
    public boolean isValid(@Nonnull String sequence) {
        return false;
    }

    @Override
    public boolean isValidLetter(char c) {
        return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
               || Character.isWhitespace(c) || (c == '-') || (c == '*') || (c == '.'));
    }
}
