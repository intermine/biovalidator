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
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.parser.GenericLineByLineParser;
import org.intermine.biovalidator.validator.AbstractValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.GenericSequenceValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.NucleicAcidSequenceValidatorWithArray;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.ProteinSequenceValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.SequenceValidator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * A validator for validating Fasta file format.
 *
 *<b>Default strategy for Fasta validator :</b>
 * 1. Validator will stop as soon as it encounter first error
 * 2. Error enabled by-default
 * 3. Warnings enabled by-default
 *
 *<b>Rules :</b>
 * 1. First Line must start with '>'
 * 2. Allow multiple sequences in a file
 * 4. Strict checking for Nucleic-Acid and Amino-Acid sequences
 * 5. Sequences letters must follow 'IUB/IUPAC'
 * 6. Warning on exceeding 80 letters in a line
 * 7. Empty files will be considered invalid
 * 8. whitespaces and empty-lines are allowed and ignored
 * 9. Each header must be unique
 * 10. Rules for Nucleotide sequences:
 *
 * @author deepak
 */
public class FastaValidator extends AbstractValidator
{
    private static final String FIRST_HEADER_MISSING_MSG = "First line must be a header line";
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

    /**
     * Construct a Fasta validator with an input source and default Sequence type
     * @param inputStreamReader input source
     */
    public FastaValidator(InputStreamReader inputStreamReader) {
        this(inputStreamReader, SequenceType.ALL);
    }

    @Nonnull
    @Override
    public ValidationResult validate() throws ValidationFailureException {
        Set<String> uniqueSequenceIds = new HashSet<>();
        DefaultValidationResult defaultValidationResult =
                (DefaultValidationResult) validationResult;
        try (Parser<String> parser = new GenericLineByLineParser(inputStreamReader)) {
            String line;
            String lastHeaderLine = "";
            long seqLengthCount = 0;
            long linesCount = 0;
            do {
                line = parser.parseNext();
                linesCount++;
                if (line != null) {
                    if (linesCount == 1 && !line.startsWith(">")) {
                        String msg = "File is not recognized as valid Fasta format";
                        validationResult.addError(ErrorMessage.of(msg));
                    }
                    else if (line.startsWith(">")) { //header TODO refactor move to a new method

                        //check whether last record had empty sequence or not
                        if (uniqueSequenceIds.size() >= 1 && seqLengthCount < 1) {
                            String msg = "Record '" + lastHeaderLine + "' has empty sequence"
                                         + " at line " + (linesCount - 1);
                            validationResult.addError(ErrorMessage.of(msg));
                        }

                        String sequenceId = extractSequenceIdFromHeader(line);
                        if (uniqueSequenceIds.contains(sequenceId)) {
                            defaultValidationResult.addError(
                                    ErrorMessage.of("Duplicate sequence-id at line " + linesCount));
                        } else {
                            uniqueSequenceIds.add(sequenceId);
                        }
                        seqLengthCount = 0;
                        lastHeaderLine = line;
                    }
                    else { //validateFasta sequence
                        line = line.trim();
                        if (linesCount < 2) {
                            ErrorMessage errorMessage = ErrorMessage.of(FIRST_HEADER_MISSING_MSG);
                            defaultValidationResult.addError(errorMessage);
                        }
                        seqLengthCount +=  sequenceValidator.validate(
                                line, linesCount, validationResult);
                    }
                    if (!validationResult.isValid()
                            && validationResultStrategy.shouldStopAtFirstError()) {
                        return validationResult;
                    }
                }
            } while (line != null);

            if (linesCount <= 1) {
                defaultValidationResult.addError(ErrorMessage.of("File is empty"));
                return validationResult;
            }

            // check whether last record has empty sequence or not
            if (seqLengthCount <= 0) {
                String msg = "Record '" + lastHeaderLine + "' has empty sequence"
                             + " at line " + linesCount;
                validationResult.addError(ErrorMessage.of(msg));
            }
        } catch (IOException e) {
            throw new ValidationFailureException(e.getMessage());
        }
        return validationResult;
    }

    private String extractSequenceIdFromHeader(String headerLine) {
        int firstSpaceIndex = -1;
        for (int i = 0; i < headerLine.length(); i++) {
            if (Character.isWhitespace(headerLine.charAt(i))) {
                firstSpaceIndex = i;
                break;
            }
        }
        return firstSpaceIndex != -1 ? headerLine.substring(0, firstSpaceIndex + 1): headerLine;
    }

    private SequenceValidator getSequenceValidatorFromType(SequenceType sequenceType) {
        switch (sequenceType) {
            case DNA:
            case RNA:
                return new NucleicAcidSequenceValidatorWithArray();
            case PROTEIN:
                return new ProteinSequenceValidator();
            case ALL:
                return new GenericSequenceValidator();
            default: throw new IllegalArgumentException("Invalid sequence type");
        }
    }
}