package org.intermine.biovalidator.validator.fasta;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.DefaultValidationResult;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.validator.AbstractValidator;

import javax.annotation.Nonnull;
import java.io.File;

/**
 * A validator for validating FASTA DNA sequences
 * @author deepak
 */
public class FastaDnaValidator extends AbstractValidator
{
    private Parser<String> parser;
    /**
     * Construct a Fasta DNA validator with a filename
     * @param filename absolute file path
     */
    public FastaDnaValidator(String filename) {
        this.file = new File(filename);
    }

    /**
     * Construct a Fasta DNA validator with a file
     * @param file file to be validated
     */
    public FastaDnaValidator(File file) {
        this.file = file;
    }

    /**
     * TODO
     * Validate DNA sequences
     * @return validation result
     * @throws ValidationFailureException if validation failed
     */
    @Nonnull
    @Override
    public ValidationResult validate() throws ValidationFailureException {
        /*try {
            //this.parser = new GenericFastaParser(new FileReader(file));
            String sequence;
            do {
                sequence = parser.parseNext();
                validateSequence(sequence);
            } while (sequence != null);
        } catch (ParsingException e) {
            throw new ValidationFailureException(e.getMessage());
        }*/
        return defaultValidationResult;
    }

    private void validateSequence(String sequence) {
    }
}
