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

import java.util.function.Consumer;

/**
 * Abstract out common functionality that each validator will have.
 *
 * @author deepak
 */
public abstract class AbstractValidator implements Validator
{
    protected ValidationResult validationResult;
    protected ValidationResultStrategy validationResultStrategy;
    protected boolean isStrict;

    /**
     * Construct validator with implementation
     */
    protected AbstractValidator() {
        this.validationResultStrategy = new DefaultValidationResultStrategy();
        this.validationResult = new DefaultValidationResult(validationResultStrategy);
    }

    @Override
    public void applyValidationResultStrategy(ValidationResultStrategy validationResultStrategy) {
        this.validationResultStrategy = validationResultStrategy;
        this.validationResult.setValidationStrategy(validationResultStrategy);
    }

    @Override
    public void validate(Consumer<ValidationResult> resultConsumer)
                                            throws ValidationFailureException {
        resultConsumer.accept(validate());
    }

    @Override
    public void applyStrictValidation() {
        isStrict = true;
    }
}
