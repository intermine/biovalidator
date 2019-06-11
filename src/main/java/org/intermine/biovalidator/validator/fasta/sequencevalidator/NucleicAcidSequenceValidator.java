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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Sequence validator for Nucleotide sequence
 * @author deepak
 */
public final class NucleicAcidSequenceValidator extends AbstractSequenceValidator
{
    private Set<Character> validSequenceLetters;

    /**
     * Construct a NucleicAcid SequenceValidator by storing all valid letters
     * in HashSet to get constant look-up cost, but the implementation will suffer
     * from Java's Autoboxing-Unboxing while look-up.
     */
    public NucleicAcidSequenceValidator() {
        String validLetters = "ACGTNUKSYMWRVBHD";
        Set<Character> seqSet = new HashSet<>();
        for (char c: validLetters.toCharArray()) {
            seqSet.add(Character.toUpperCase(c));
            seqSet.add(Character.toLowerCase(c));
        }
        seqSet.add('-');
        this.validSequenceLetters = Collections.unmodifiableSet(seqSet);
    }

    @Override
    public boolean isValidLetter(char c) {
        return validSequenceLetters.contains(c);
    }
}
