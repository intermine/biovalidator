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

/**
 * Sequence validator for Nucleotide sequence
 *
 * @author deepak
 */
public final class NucleicAcidSequenceValidatorWithArray extends AbstractSequenceValidator
{
    private static final int CONTAINER_SIZE = 26;
    private boolean[] validSequenceLetters;

    /**
     * Construct a NucleicAcid SequenceValidator by storing all valid letters
     * in raw array of the english alphabet size, as all valid NucleicAcid
     * letters as valid english alphabet except '-'. This implementation will
     * have constant look-up time complexity without Autoboxing-Unboxing overhead
     * as compared to the class NucleicAcidSequenceValidator
     * <p>
     *     Currently this implementation is used for validating NucleicAcid sequences
     * </p>
     */
    public NucleicAcidSequenceValidatorWithArray() {
        String validLetters = "ACGTNUKSYMWRVBHD";
        validSequenceLetters = new boolean[CONTAINER_SIZE];
        for (char c: validLetters.toCharArray()) {
            int alphabetIndex = getAlphabetIndex(c);
            if ( (alphabetIndex >= 0) && (alphabetIndex <= CONTAINER_SIZE)) {
                validSequenceLetters[alphabetIndex] = true;
            }
        }
    }

    @Override
    public boolean isValidLetter(char c) {
        int indx = getAlphabetIndex(c);
        if (indx >= 0 && indx < CONTAINER_SIZE) {
            return validSequenceLetters[indx];
        }
        return (c == '-') || Character.isWhitespace(c);
    }

    private int getAlphabetIndex(char c) {
        return (Character.toUpperCase(c) - 'A');
    }
}
