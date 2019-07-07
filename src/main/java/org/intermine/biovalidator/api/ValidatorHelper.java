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

import org.intermine.biovalidator.validator.ValidatorType;

import javax.annotation.Nonnull;

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
     * validates a file
     * @param file filename with full path to be validated
     * @param validatorType type of validator
     * @param isStrict validate strictly or not
     * @return validation result
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validate(@Nonnull String file,
                                            @Nonnull ValidatorType validatorType,
                                            boolean isStrict)
            throws ValidationFailureException {
        ValidatorBuilder builder  = ValidatorBuilder.withFile(file, validatorType)
                .withStrictValidation(isStrict);
        return builder.build().validate();
    }

    /**
     * validates a file
     * @param file filename with full path to be validated
     * @param validatorType type of validator
     * @param isStrict validate strictly or not
     * @return validation result
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validate(@Nonnull String file,
                                            @Nonnull String validatorType,
                                            boolean isStrict)
            throws ValidationFailureException {
        return validate(file, ValidatorType.of(validatorType), isStrict);
    }

    /**
     * A helper method to create a validator
     * @param file file to be validated
     * @param validatorType type of validator type
     * @param isStrict whether to validate strictly or not
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFasta(@Nonnull String file,
                                                 @Nonnull ValidatorType validatorType,
                                                 boolean isStrict)
            throws ValidationFailureException {
        return validate(file, validatorType, isStrict);
    }

    /**
     * Builds a DNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaDna(@Nonnull String file)
            throws ValidationFailureException {
        return validateFasta(file, ValidatorType.FASTA_DNA, true);
    }

    /**
     * Builds a RNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaRna(@Nonnull String file)
            throws ValidationFailureException {
        return validateFasta(file, ValidatorType.FASTA_RNA, true);
    }

    /**
     * Builds a PROTEIN sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaProtein(@Nonnull String file)
            throws ValidationFailureException {
        return validateFasta(file, ValidatorType.FASTA_PROTEIN, true);
    }

    /**
     * Validates a fasta file for any type of sequence
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFasta(@Nonnull String file)
            throws ValidationFailureException {
        return validateFasta(file, ValidatorType.FASTA, true);
    }

    /**
     * Validate a Gff3 file with string mode
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException is validation fails
     */
    public static ValidationResult validateGff3(@Nonnull String file)
            throws ValidationFailureException {
        return validate(file, ValidatorType.GFF3, true);
    }

    /**
     * Validate a gff3 file with a given mode(strict or permissive)
     * @param file file to be validated
     * @param isStrict whether to validate in strict mode or permissive
     * @return result of validation
     * @throws ValidationFailureException if validation failes
     */
    public static ValidationResult validateGff3(@Nonnull String file, boolean isStrict)
            throws ValidationFailureException {
        return validate(file, ValidatorType.GFF3, isStrict);
    }

}

