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
public class ValidationResult
{
    /**
     * list of validation messages
     */
    private List<Message> errorMessages;
    private List<Message> warningMessages;

    /**
     * Default ValidationResult Constructor
     */
    public ValidationResult() {
        this.errorMessages = new ArrayList<>();
        this.warningMessages = new ArrayList<>();
    }

    /**
     * Return total number of warnings found
     * @return total warnings
     */
    public int totalWarnings() {
        return warningMessages.size();
    }

    /**
     * Return total number of errors found
     * @return total errors
     */
    public int totalError() {
        return errorMessages.size();
    }


    /**
     * Gets list of validation messages.
     *
     * @return Value of list of validation messages.
     */
    public List<Message> getErrorMessages() {
        return errorMessages;
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
     * Gets warningMessages.
     *
     * @return Value of warningMessages.
     */
    public List<Message> getWarningMessages() {
        return warningMessages;
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
