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

import java.util.List;

/**
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
     * Return list of warning messages.
     *
     * @return Value of warning messages.
     */
    List<Message> getWarningMessages();

    /**
     * Return whether validator has valid result or not
     * @return test result
     */
    boolean isValid();

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
}
