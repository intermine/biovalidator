package org.intermine.biovalidator.validator.fasta;

import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.validator.AbstractValidator;
import org.intermine.biovalidator.validator.fasta.sequencevalidator.SequenceValidator;

import javax.annotation.Nonnull;
import java.io.File;

public class FastaValidator extends AbstractValidator
{
    private Parser<String> parser;
    private SequenceValidator sequenceValidator;

    /**
     * Construct a Fasta DNA validator with a filename
     * @param filename absolute file path
     */
    public FastaValidator(String filename) {
        this.file = new File(filename);
    }

    /**
     * Construct a Fasta DNA validator with a file
     * @param file file to be validated
     */
    public FastaValidator(File file) {
        this.file = file;
    }

    /**
     * Construct a Fasta validator with a sequence validator and a filename
     * @param filename absolute file path
     * @param sequenceValidator
     */
    public FastaValidator(String filename, SequenceValidator sequenceValidator) {
        this.file = new File(filename);
        this.sequenceValidator = sequenceValidator;
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
        return null;
    }
}
