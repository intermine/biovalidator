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
import org.intermine.biovalidator.api.ErrorMessage;
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
import java.util.HashSet;
import java.util.Set;

public class FastaValidator extends AbstractValidator
{
    private static final String INVALID_SEQUENCE_START_MSG = "First line must be a header line";
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
        Set<String> uniqueSequenceIds = new HashSet<>();
        DefaultValidationResult defaultValidationResult =
                (DefaultValidationResult) validationResult;
        try {
            this.parser = new GenericFastaParser(inputStreamReader);
            String line;
            String currentHeader = "";
            long linesCount = 0;
            do {
                line = parser.parseNext();
                linesCount++;
                if (line != null) {
                    if (line.startsWith(">")) { //header
                        String sequenceId = line; //line.substring(0, line.lastIndexOf('|'));
                        if (uniqueSequenceIds.contains(sequenceId)) {
                            defaultValidationResult.addError(
                                    ErrorMessage.of("Duplicate sequence-id at line " + linesCount));
                        } else {
                            uniqueSequenceIds.add(sequenceId);
                            currentHeader = line;
                        }
                    } else { //validate sequence
                        if (linesCount < 2) {
                            defaultValidationResult.addError(
                                    ErrorMessage.of(INVALID_SEQUENCE_START_MSG));
                        }
                        long seqLengthCount = 0;
                        while (line != null && !line.startsWith(">")) {
                            seqLengthCount +=  sequenceValidator.validate(line,
                                    linesCount, validationResult);
                            line = parser.parseNext();
                            linesCount++;
                        }
                        if (seqLengthCount < 1) { //empty record
                            String msg = "Record '" + currentHeader + "' has empty sequence";
                            validationResult.addError(ErrorMessage.of(msg));
                        }
                    }

                    if (!validationResult.isValid()
                            && validationResultStrategy.shouldStopAtFirstError()) {
                        return validationResult;
                    }
                }
            } while (line != null);
            //System.out.println("Total Lines Read : " + linesCount);
        } catch (ParsingException e) {
            throw new ValidationFailureException(e.getMessage());
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
