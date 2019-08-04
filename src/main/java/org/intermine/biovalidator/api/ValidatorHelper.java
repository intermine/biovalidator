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

import org.apache.commons.lang3.StringUtils;
import org.intermine.biovalidator.validator.ValidatorType;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.Optional;

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
 * Provides helper methods create validator.
 * Example:
 * <code>
 *      1. ValidatorHelper.validate("file", "fasta-dna", false)
 *      2. Or Simply:
 *         ValidatorHelper.validateFastaDna("file")
 * </code>
 * @author deepak
 */
public final class ValidatorHelper
{
    private ValidatorHelper() { }

    /**
     * validates a file and always returns a ValidationResult, in case of any runtime exception
     * it will catch and return ValidationResult with error message of the exception
     * @param file filename with full path to be validated
     * @param validatorType type of validator
     * @param isStrict validate strictly or not
     * @return validation result
     */
    public static ValidationResult validate(@Nonnull String file,
                                            @Nonnull ValidatorType validatorType,
                                            boolean isStrict) {
        try {
            ValidatorBuilder builder = ValidatorBuilder.withFile(file, validatorType)
                    .withStrictValidation(isStrict);
            return builder.build().validate();
        } catch (RuntimeException e) {
            return createValidationResultWithError(e.getMessage());
        }
    }

    /**
     * validates a file
     * @param file filename with full path to be validated
     * @param validatorType type of validator
     * @param isStrict validate strictly or not
     * @return validation result
     */
    public static ValidationResult validate(@Nonnull String file,
                                            @Nonnull String validatorType,
                                            boolean isStrict) {
        ValidatorType type = null;
        try {
            type = ValidatorType.of(validatorType);
        } catch (IllegalArgumentException e) {
            // try to guess validator type from given filename
            Optional<ValidatorType> typeOptional = guessValidatorType(file);
            if (typeOptional.isPresent()) {
                type = typeOptional.get();
            } else {
                String errMsg = "Invalid Validator type! It must be one of ("
                        + Arrays.toString(ValidatorType.values()) + "), case-insensitive.";
                return createValidationResultWithError(errMsg);
            }
        }
        return validate(file, type, isStrict);
    }

    /**
     * A helper method to create a validator
     * @param file file to be validated
     * @param validatorType type of validator type
     * @param isStrict whether to validate strictly or not
     * @return result of validation
     */
    public static ValidationResult validateFasta(@Nonnull String file,
                                                 @Nonnull ValidatorType validatorType,
                                                 boolean isStrict) {
        return validate(file, validatorType, isStrict);
    }

    /**
     * Builds a DNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     */
    public static ValidationResult validateFastaDna(@Nonnull String file) {
        return validateFasta(file, ValidatorType.FASTA_DNA, true);
    }

    /**
     * Builds a RNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     */
    public static ValidationResult validateFastaRna(@Nonnull String file) {
        return validateFasta(file, ValidatorType.FASTA_RNA, true);
    }

    /**
     * Builds a PROTEIN sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     */
    public static ValidationResult validateFastaProtein(@Nonnull String file) {
        return validateFasta(file, ValidatorType.FASTA_PROTEIN, true);
    }

    /**
     * Validates a fasta file for any type of sequence
     * @param file file to be validated
     * @return result of validation
     */
    public static ValidationResult validateFasta(@Nonnull String file) {
        return validateFasta(file, ValidatorType.FASTA, true);
    }

    /**
     * Validate a Gff3 file with string mode
     * @param file file to be validated
     * @return result of validation
     */
    public static ValidationResult validateGff3(@Nonnull String file) {
        return validate(file, ValidatorType.GFF3, true);
    }

    /**
     * Validate a gff3 file with a given mode(strict or permissive)
     * @param file file to be validated
     * @param isStrict whether to validate in strict mode or permissive
     * @return result of validation
     */
    public static ValidationResult validateGff3(@Nonnull String file, boolean isStrict) {
        return validate(file, ValidatorType.GFF3, isStrict);
    }

    /**
     * Validate a gff3 file with a given mode(strict or permissive)
     *
     * @param file     file to be validated
     * @param isStrict whether to validate in strict mode or permissive
     * @return result of validation
     */
    public static ValidationResult validateCsv(@Nonnull String file, boolean isStrict) {
        try {
            return ValidatorBuilder.withFile(file, ValidatorType.CSV)
                    .enableWarnings()
                    .build()
                    .validate();
        } catch (RuntimeException e) {
            return createValidationResultWithError(e.getMessage());
        }
    }

    /**
     * Validate a gff3 file with a given mode(strict or permissive)
     *
     * @param file      file to be validated
     * @param isStrict  whether to validate in strict mode or permissive
     * @param delimiter delimiter for column separator
     * @return result of validation
     * @throws ValidationFailureException if validation failes
     */
    public static ValidationResult validateCsv(@Nonnull String file,
                                               String delimiter, boolean isStrict)
            throws ValidationFailureException {
        return validate(file, ValidatorType.CSV, isStrict);
    }

    private static ValidationResult createValidationResultWithError(String message) {
        ValidationResult result = new DefaultValidationResult(
                new DefaultValidationResultStrategy());
        result.addError(message);
        return result;
    }


    /**
     * Guess validator type from filename(i.e. filename's extension) if available
     * @param filename absolute file path
     * @return optional of validator type
     */
    private static Optional<ValidatorType> guessValidatorType(String filename) {
        Optional<String> extensionOpt = getFileExtension(filename);
        if (!extensionOpt.isPresent()) {
            return Optional.empty();
        }
        String foundExtension = extensionOpt.get();
        //try to match found extension with supported-extensions of all available validator types
        for (ValidatorType validatorType : ValidatorType.values()) {
            if (validatorType.getSupportedFileExtensions().contains(foundExtension)) {
                return Optional.of(validatorType);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets extension of a file
     * @param filename filename
     * @return optional of file extension
     */
    private static Optional<String> getFileExtension(String filename) {
        int extensionIndex = filename.lastIndexOf(".");
        int separatorIndex = filename.lastIndexOf(File.separator);
        if (extensionIndex == -1 || extensionIndex < separatorIndex) {
            return Optional.empty();
        }
        return Optional.of(filename.substring(extensionIndex + 1));
    }
}

