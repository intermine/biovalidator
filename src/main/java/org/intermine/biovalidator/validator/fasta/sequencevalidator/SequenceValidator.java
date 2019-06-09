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

import org.intermine.biovalidator.api.ValidationResult;

import javax.annotation.Nonnull;

/**
 * An immutable sequence validator
 * @author deepak
 */
public interface SequenceValidator
{
    /**
     * Test whether a sequence is valid or not.
     * Returns a boolean and can optionally add validation error to ValidationResult
     * @param sequence sequence that is to be validated
     * @param seqLineNo line number of the sequence inside the source data,
     *                  it can be used to reference the line number of the
     *                  error while creating validation failure message
     * @param result validation result where validation error can be added
     * @return return boolean indicating failure or success
     */
    boolean validate(@Nonnull String sequence, int seqLineNo, @Nonnull ValidationResult result);

    /**
     * Test whether a sequence is valid or not
     * @param sequence sequence to be validated
     * @return validation result
     */
    boolean isValid(@Nonnull String sequence);

    /**
     * Test whether a particular letter is valid or not
     * @param c letter to be validated
     * @return returns whether letter is valid or not
     */
    boolean isValidLetter(char c);
}
