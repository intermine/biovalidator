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
 * @author deepak
 */
public final class NucleicAcidSequenceValidator extends AbstractSequenceValidator
{
    /**
     * Initialize with valid sequence letters
     */
    public NucleicAcidSequenceValidator() {
        super("ACGTNUKSYMWRBDHV-");
    }
}
