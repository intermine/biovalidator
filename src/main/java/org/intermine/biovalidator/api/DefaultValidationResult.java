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

import java.util.ArrayList;
import java.util.List;

/**
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

    /**
     * Default DefaultValidationResult Constructor
     */
    public DefaultValidationResult() {
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
        return errorMessages;
    }

    @Override
    public List<Message> getWarningMessages() {
        return warningMessages;
    }

    @Override
    public boolean isValid() {
        return isValid;
    }

    /**
     * Sets validation result is valid or not.
     *
     * @param valid either true or false
     */
    public void setValid(boolean valid) {
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
