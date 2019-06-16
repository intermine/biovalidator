package org.intermine.biovalidator.api;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.biovalidator.api.strategy.ValidationResultStrategy;
import org.intermine.biovalidator.validator.ValidatorType;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Builder class to instantiate a Validator with customization
* @author deepak
 */
public final class ValidatorBuilder
{
    private Validator validator;
    private ValidationResultStrategy validationResultStrategy;

    private ValidatorBuilder() {
        this.validationResultStrategy = new DefaultValidationResultStrategy();
    }
    /**
     * Builds a validator implementation
     * @return an implementation of builder
     */
    public Validator build() {
        this.validator.applyValidationResultStrategy(validationResultStrategy);
        return validator;
    }

    /**
     * Construct a ValidatorBuilder with validator instance
     * @param validator instance of validator
     * @return ValidatorBuilder
     */
    public static ValidatorBuilder ofType(@Nonnull Validator validator) {
        ValidatorBuilder builder = new ValidatorBuilder();
        builder.validator = validator;
        return builder;
    }

    /**
     * A factory method to construct validator with a file based on the type argument
     * @param filename absolute file path
     * @param type used to identify validator instance
     * @return ValidatorBuilder
     * @throws IllegalArgumentException if type not found
     */
    public static ValidatorBuilder withFile(@Nonnull File filename,
                                            @Nonnull ValidatorType type)
                                                throws IllegalArgumentException {
        FileReader reader;
        try {
            reader = new FileReader(filename);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("file not found");
        }
        switch (type) {
            case FASTA:
                return ofType(new FastaValidator(reader, SequenceType.ALL));
            case FASTA_DNA:
                return ofType(new FastaValidator(reader, SequenceType.DNA));
            case FASTA_RNA:
                return ofType(new FastaValidator(reader, SequenceType.RNA));
            case FASTA_PROTEIN:
                return ofType(new FastaValidator(reader, SequenceType.PROTEIN));
            default:
                throw new IllegalArgumentException("invalid validator type");
        }
    }

    /**
     * A factory method to construct validator with a file based on the type argument
     * @param file file to be validated
     * @param type used to identify validator instance
     * @return ValidatorBuilder
     * @throws IllegalArgumentException if type not found
     */
    public static ValidatorBuilder withFile(@Nonnull String file, @Nonnull ValidatorType type)
                                                throws IllegalArgumentException {
        return withFile(new File(file), type);
    }

    /**
     * A factory method to construct validator with a file based on the validator-type
     * argument as string
     * @param file file to be validated
     * @param validatorType string representation of ValidatorType
     * @return ValidatorBuilder
     * @throws IllegalArgumentException if type not found
     */
    public static ValidatorBuilder withFile(@Nonnull String file, @Nonnull String validatorType)
            throws IllegalArgumentException {
        return withFile(new File(file), ValidatorType.of(validatorType));
    }

    /**
     * set the validator to stop validating as soon as it identifies first error
     * @return ValidatorBuilder
     */
    public ValidatorBuilder enableStopAtFirstError() {
        this.validationResultStrategy.enableStopAtFirstError();
        return this;
    }

    /**
     * Disable stop at first error
     * @return ValidatorBuilder
     */
    public ValidatorBuilder disableStopAtFirstError() {
        this.validationResultStrategy.disableStopAtFirstError();
        return this;
    }

    /**
     * Enables warning messages to be create by the validator
     * @return ValidatorBui
     */
    public ValidatorBuilder enableWarnings() {
        this.validationResultStrategy.enableWarnings();
        return this;
    }

    /**
     * Disable warning messages
     * @return ValidatorBuilder
     */
    public ValidatorBuilder disableWarnings() {
        this.validationResultStrategy.disableWarnings();
        return this;
    }

    /**
     * Enables error messages to be create by the validator
     * @return ValidatorBuilder
     */
    public ValidatorBuilder enableErrors() {
        this.validationResultStrategy.enableWarnings();
        return this;
    }

    /**
     * Disable error messages
     * @return ValidatorBuilder
     */
    public ValidatorBuilder disableErrors() {
        this.validationResultStrategy.disableErrors();
        return this;
    }

    /**
     * Enable strict validation
     * @return ValidatorBuilder
     */
    public ValidatorBuilder enableStrictValidation() {
        this.validator.applyStrictValidation();
        return this;
    }
}
