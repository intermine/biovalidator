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

/**
 * This class provides a skeletal implementation of SequenceValidator
 * which other implementation can use the common features
 * @author deepak
 */
public abstract class AbstractSequenceValidator implements SequenceValidator
{
    @Override
    public int validate(@Nonnull String sequence, long seqLineNo,
                            @Nonnull ValidationResult validationResult) {
        for (int i = 0; i < sequence.length(); i++) {
            if (!isValidLetter(sequence.charAt(i))) {
                String msg = "Invalid letter " + sequence.charAt(i)
                        + " at line number " + seqLineNo + ", column " + (i + 1);
                validationResult.addError(ErrorMessage.of(msg));
                return (i + 1);
            }
        }
        if (sequence.length() > 80) {
            validationResult.addWarning(WarningMessage.of("number of sequence length "
                    + "exceed 80 at line " + seqLineNo));
        }
        return sequence.length();
    }

    @Override
    public boolean isValid(@Nonnull String seq) {
        for (char c: seq.toCharArray()) {
            if (!isValidLetter(c)) {
                return false;
            }
        }
        return true;
    }
}
