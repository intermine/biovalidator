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

public final class ProteinSequenceValidator extends AbstractSequenceValidator
{
    /**
     * Initialize with valid sequence letters
     */
    public ProteinSequenceValidator() {
        super("ARNDCQEGHILKMFFPSTWYVBZX*-");
    }
}
