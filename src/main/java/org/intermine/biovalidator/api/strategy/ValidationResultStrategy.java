package org.intermine.biovalidator.api.strategy;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

/**
 * Defines strategy for result of validation
 * @author deepak
 */
public interface ValidationResultStrategy
{
    /**
     * Enables creating warning messages
     */
    void enableWarnings();

    /**
     * Disable warning messages
     */
    void disableWarnings();

    /**
     * Enable creating error message
     */
    void enableErrors();

    /**
     * Disable error messages
     */
    void disableErrors();

    /**
     * Indicate validator to stop as soon as an error occurred
     */
    void stopAtFirstError();

    /**
     * Returns whether error is enabled or not
     * @return error enabled state
     */
    boolean isErrorEnabled();

    /**
     * Returns whether warning enabled or not
     * @return warning enabled state
     */
    boolean isWarningWnabled();

    /**
     * Retunrs whether validator should stop at first error or not
     * @return should stop at first error or not
     */
    boolean shouldStopAtFirstError();
}
