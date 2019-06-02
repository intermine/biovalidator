package org.intermine.biovalidator.validator;

import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidationResultStrategy;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorStrictnessPolicy;

public abstract class AbstractValidator implements Validator
{
    protected ValidationResult validationResult;
    protected ValidationResultStrategy validationResultStrategy;
    protected ValidatorStrictnessPolicy validatorStrictnessPolicy;

    /**
     * Construct validator with implementation
     */
    public AbstractValidator() {
        this.validationResultStrategy = ValidationResultStrategy.getDefaultStrategy();
        this.validatorStrictnessPolicy = null;
        this.validationResult = new ValidationResult();
    }

    @Override
    public void applyValidationResultStrategy(ValidationResultStrategy validationResultStrategy) {
        this.validationResultStrategy = validationResultStrategy;

    }

    @Override
    public void applyValidatorStrictnessPolicy(ValidatorStrictnessPolicy strictnessPolicy) {
        this.validatorStrictnessPolicy = strictnessPolicy;
    }
}
