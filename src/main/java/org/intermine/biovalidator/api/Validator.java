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

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * A common interface for all validators, any validator will an implementation of this interface.
 * Validator provides a validate() method that user of the interface can use to perform validation,
 *
 * Validator also allows to customize validation strategy.
 * Example:
 *  1. Enable Errors (default)
 *  2. Enable Warnings
 *  3. customize whether validator should stop when it encounter first error or not, etc..
 *
 * @author deepak
 */
public interface Validator
{
    /**
     * Validates a file
     * @return A detailed result of the validation
     * @throws ValidationFailureException if validator failed to validateFasta a given file.
     */
    @Nonnull ValidationResult validate() throws ValidationFailureException;

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
     * apply strict validation
     */
    void applyStrictValidation();
}
