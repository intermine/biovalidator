package org.intermine.biovalidator.api;


public interface Validator
{
    /**
     * Validates a sequence file
     * @return A detailed result of the validation
     * @throws ValidationFailureException if validator failed to validate a given file.
     */
    ValidationResult validate() throws ValidationFailureException;

    /**
     * Apply strategy for custom validation result
     * @param validationResultStrategy strategy for validation result
     */
    void applyValidationResultStrategy(ValidationResultStrategy validationResultStrategy);

    /**
     * Apply setting on how strict validator will be
     * @param strictnessPolicy strictness policy
     */
    void applyValidatorStrictnessPolicy(ValidatorStrictnessPolicy strictnessPolicy);
}
