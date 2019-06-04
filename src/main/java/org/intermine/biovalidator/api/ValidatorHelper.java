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

import javax.annotation.Nonnull;

/**
 * Provides helper methods create validator.
 * Example:
 * <code>
 *      1. ValidatorHelper.validate("file", Type.FASTA, SequenceType.DNA, false)
 *      2. Or Simply:
 *         ValidatorHelper.validateFastaDna("file")
 * </code>
 * @author deepak
 */
public final class ValidatorHelper
{
    private ValidatorHelper() { }

    /**
     * A helper method to create a validator
     * @param file file to be validated
     * @param fileType type of biological file
     * @param isStrict setting on how strict validator should be
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validate(@Nonnull String file,
                                            @Nonnull ValidatorBuilder.Type fileType,
                                            boolean isStrict)
                                            throws ValidationFailureException {
        return ValidatorBuilder.withFile(file, fileType).build().validate();
    }

    /**
     * Builds a DNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaDna(@Nonnull String file)
                                                    throws ValidationFailureException {
        return validate(file, ValidatorBuilder.Type.FASTA_DNA, false);
    }

    /**
     * Builds a RNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaRna(@Nonnull String file)
            throws ValidationFailureException {
        return validate(file, ValidatorBuilder.Type.FASTA_RNA, false);
    }

    /**
     * Builds a PROTEIN sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaProtein(@Nonnull String file)
            throws ValidationFailureException {
        return validate(file, ValidatorBuilder.Type.FASTA_PROTEIN, false);
    }

}

