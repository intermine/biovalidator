package org.intermine.biovalidator.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
     * @param seqType type of sequence, only applicable for file format which
     *                support multiple types such as FATSA support DNA, RNA, PROTEIN.
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validate(@Nonnull String file,
                                            @Nonnull Type fileType,
                                            @Nullable SequenceType seqType,
                                            boolean isStrict)
                                            throws ValidationFailureException {
        return ValidatorBuilder.build().validate();
    }

    /**
     * Builds a DNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaDna(@Nonnull String file)
                                                    throws ValidationFailureException {
        return validate(file, Type.FASTA, SequenceType.DNA, false);
    }

    /**
     * Builds a RNA sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaRna(@Nonnull String file)
            throws ValidationFailureException {
        return validate(file, Type.FASTA, SequenceType.DNA, false);
    }

    /**
     * Builds a PROTEIN sequence fasta validator
     * @param file file to be validated
     * @return result of validation
     * @throws ValidationFailureException if validation fails
     */
    public static ValidationResult validateFastaProtein(@Nonnull String file)
            throws ValidationFailureException {
        return validate(file, Type.FASTA, SequenceType.DNA, false);
    }

    public enum Type
    {
        /**
         * Represents a type for FASTA
         */
        FASTA
    }

    public enum SequenceType {
        /**
         * sequence type
         */
        DNA, RNA, PROTEIN
    }
}

