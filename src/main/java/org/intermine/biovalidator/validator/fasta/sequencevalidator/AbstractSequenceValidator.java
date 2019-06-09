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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractSequenceValidator implements SequenceValidator
{
    private Set<Character> validSequenceLetters;

    /**
     * Initialize with valid sequence letters
     * @param validLetters valid sequences
     */
    AbstractSequenceValidator(String validLetters) {
        Set<Character> validLettersSet = new HashSet<>();
        for (char c: validLetters.toCharArray()) {
            validLettersSet.add(c);
        }
        this.validSequenceLetters = Collections.unmodifiableSet(validLettersSet);
    }

    @Override
    public boolean validate(@Nonnull String seq, long lineNo,
                            @Nonnull ValidationResult validationResult) {
        for (int i = 0; i < seq.length(); i++) {
            if (!isValidLetter(seq.charAt(i))) {
                String msg = "Invalid letter " + seq.charAt(i)
                        + " at line number " + lineNo + ", column " + (i + 1);
                validationResult.getErrorMessages().add(new ErrorMessage(msg));
                return false;
            }
        }
        return true;
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

    @Override
    public boolean isValidLetter(char c) {
        return validSequenceLetters.contains(c)
                || validSequenceLetters.contains(Character.toUpperCase(c));
    }
}
