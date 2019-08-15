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

import java.util.List;

/**
 * This interface represents the result of a validation, instance of this will be returned on
 * calling validate method any Validator
 *
 * @author deepak
 */
public interface ValidationResult
{
    /**
     * Return total number of warnings found
     * @return total warnings
     */
    int totalWarnings();

    /**
     * Return total number of errors found
     * @return total errors
     */
    int totalError();

    /**
     * Return list of validation messages.
     *
     * @return Value of list of validation messages.
     */
    List<Message> getErrorMessages();

    /**
     * Returns a single error message as string describing the cause of validation failure,
     * if there are more than one error message then it returns the first error message.
     * if validator failed or else it returns an empty string
     * @return string containing error message or empty string
     */
    String getErrorMessage();

    /**
     * Return list of warning messages.
     *
     * @return Value of warning messages.
     */
    List<Message> getWarningMessages();

    /**
     * Test whether validator has valid result or not
     * @return true if result is valid
     */
    boolean isValid();

    /**
     * Test whether is valid or not
     * @return true if result is not valid
     */
    boolean isNotValid();

    /**
     * Add a error message
     * @param errorMessage message
     */
    void addError(ErrorMessage errorMessage);

    /**
     * Add a warning message
     * @param warningMessage message
     */
    void addWarning(WarningMessage warningMessage);

    /**
     * Add a error with string as the message
     * @param errorMessage error
     */
    void addError(String errorMessage);

    /**
     * Add a warning with string as the message
     * @param warningMessage warning
     */
    void addWarning(String warningMessage);

    /**
     * Sets validation result strategy
     * @param strategy strategy to be used while validation
     */
    void setValidationStrategy(ValidationResultStrategy strategy);
}
