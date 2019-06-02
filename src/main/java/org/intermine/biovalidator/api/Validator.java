package org.intermine.biovalidator.api;


import java.util.function.Consumer;

public interface Validator
{
    /**
     * Validates a file
     * @return A detailed result of the validation
     * @throws ValidationFailureException if validator failed to validate a given file.
     */
    ValidationResult validate() throws ValidationFailureException;

    /**
     * Validates a file and pass the result to a consumer
     * @param resultConsumer the consumer which will accept the result
     * @throws ValidationFailureException if validation fails
     */
    void validate(Consumer<ValidationResult> resultConsumer) throws ValidationFailureException;

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
