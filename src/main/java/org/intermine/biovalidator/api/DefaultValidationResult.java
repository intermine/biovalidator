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

import org.apache.commons.lang3.StringUtils;
import org.intermine.biovalidator.api.strategy.ValidationResultStrategy;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple and default implementation of ValidationResult
 *
 * @author deepak
 */
public class DefaultValidationResult implements ValidationResult
{
    /**
     * list of validation messages
     */
    private List<Message> errorMessages;
    private List<Message> warningMessages;
    private boolean isValid;
    private ValidationResultStrategy resultStrategy;

    /**
     * Default DefaultValidationResult Constructor
     * @param resultStrategy strategy for validation result
     */
    public DefaultValidationResult(ValidationResultStrategy resultStrategy) {
        this.resultStrategy = resultStrategy;
        this.errorMessages = new ArrayList<>();
        this.warningMessages = new ArrayList<>();
        this.isValid = true;
    }

    @Override
    public int totalWarnings() {
        return warningMessages.size();
    }

    @Override
    public int totalError() {
        return errorMessages.size();
    }


    @Override
    public List<Message> getErrorMessages() {
        return Collections.unmodifiableList(errorMessages);
    }

    @Override
    public String getErrorMessage() {
        //return first error msg its in invalid state else return empty string
        if (!isValid && errorMessages.size() > 0) {
            Message errMsg = errorMessages.get(0);
            if (errMsg != null) {
                return errMsg.getMessage();
            }
        }
        return StringUtils.EMPTY;
    }

    @Override
    public List<Message> getWarningMessages() {
        return Collections.unmodifiableList(warningMessages);
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    @Override
    public void addError(@Nonnull ErrorMessage errorMessage) {
        this.isValid = false;
        if (resultStrategy.isErrorEnabled()) {
            this.errorMessages.add(errorMessage);
        }
    }

    @Override
    public void addWarning(@Nonnull WarningMessage warningMessage) {
        if (resultStrategy.isWarningEnabled()) {
            this.warningMessages.add(warningMessage);
        }
    }

    @Override
    public void addError(String errorMessage) {
        addError(ErrorMessage.of(errorMessage));
    }

    @Override
    public void addWarning(String warningMessage) {
        addWarning(WarningMessage.of(warningMessage));
    }

    @Override public void setValidationStrategy(ValidationResultStrategy strategy) {
        this.resultStrategy = strategy;
    }

    /**
     * Gets validation result strategy
     * @return validation result strategy
     */
    public ValidationResultStrategy getResultStrategy() {
        return resultStrategy;
    }

    /**
     * Sets validation result is valid or not.
     *
     * @param valid either true or false
     */
    public void setIsValid(boolean valid) {
        isValid = valid;
    }

    /**
     * Sets new list of validation messages.
     *
     * @param errorMessages New value of list of validation messages.
     */
    public void setErrorMessages(List<Message> errorMessages) {
        this.errorMessages = errorMessages;
    }

    /**
     * Sets new warningMessages.
     *
     * @param warningMessages New value of warningMessages.
     */
    public void setWarningMessages(List<Message> warningMessages) {
        this.warningMessages = warningMessages;
    }
}
