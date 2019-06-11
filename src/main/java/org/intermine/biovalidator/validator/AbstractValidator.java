package org.intermine.biovalidator.validator;

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
import org.intermine.biovalidator.api.DefaultValidationResultStrategy;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.strategy.ValidationResultStrategy;
import org.intermine.biovalidator.api.strategy.ValidatorStrictnessPolicy;

import java.io.File;
import java.util.function.Consumer;

/**
 * @author deepak
 */
public abstract class AbstractValidator implements Validator
{
    protected ValidationResult validationResult;
    protected ValidationResultStrategy validationResultStrategy;
    protected ValidatorStrictnessPolicy validatorStrictnessPolicy;
    protected File file;

    /**
     * Construct validator with implementation
     */
    public AbstractValidator() {
        this.validationResultStrategy = new DefaultValidationResultStrategy();
        this.validationResult = new DefaultValidationResult(validationResultStrategy);
    }

    @Override
    public void applyValidationResultStrategy(ValidationResultStrategy validationResultStrategy) {
        this.validationResultStrategy = validationResultStrategy;

    }

    @Override
    public void applyValidatorStrictnessPolicy(ValidatorStrictnessPolicy strictnessPolicy) {
        this.validatorStrictnessPolicy = strictnessPolicy;
    }

    @Override
    public void validate(Consumer<ValidationResult> resultConsumer)
                                            throws ValidationFailureException {
        resultConsumer.accept(validate());
    }
}
