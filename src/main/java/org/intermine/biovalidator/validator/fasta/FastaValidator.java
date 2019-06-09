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

import org.intermine.biovalidator.api.DefaultValidationResult;
import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ParsingException;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.parser.GenericFastaParser;
import org.intermine.biovalidator.validator.AbstractValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.GenericSequenceValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.NucleicAcidSequenceValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.ProteinSequenceValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.SequenceValidator;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;

public class FastaValidator extends AbstractValidator
{
    private Parser<String> parser;
    private SequenceValidator sequenceValidator;
    private InputStreamReader inputStreamReader;

    /**
     * Construct a Fasta validator with an input source and sequence type
     * @param inputStreamReader data source
     * @param sequenceType type of sequence to be validated
     */
    public FastaValidator(InputStreamReader inputStreamReader, SequenceType sequenceType) {
        this.inputStreamReader = inputStreamReader;
        this.sequenceValidator = getSequenceValidatorFromType(sequenceType);
    }

    @Nonnull
    @Override
    public ValidationResult validate() throws ValidationFailureException {
        try {
            this.parser = new GenericFastaParser(inputStreamReader);
            String line;
            long lines = 0, chars = 0;
            long log = 0;
            do {
                line = parser.parseNext();
                lines++;
                if (line != null && !line.startsWith(">")) {
                    boolean isValid = sequenceValidator.validate(line, lines, validationResult);
                    if (!isValid) {
                        System.out.println("Total Lines Read : " + lines + " " + isValid);
                        ((DefaultValidationResult) validationResult).setValid(false);
                        return validationResult;
                    }
                }
            } while (line != null);
            System.out.print(log);
            System.out.println("Total Lines Read : " + lines);
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        return validationResult;
    }

    private SequenceValidator getSequenceValidatorFromType(SequenceType sequenceType) {
        switch (sequenceType) {
            case DNA:
            case RNA:
                return new NucleicAcidSequenceValidator();
            case PROTEIN:
                return new ProteinSequenceValidator();
            case ALL:
                return new GenericSequenceValidator();
            default: throw new IllegalArgumentException("Invalid sequence type");
        }
    }
}
