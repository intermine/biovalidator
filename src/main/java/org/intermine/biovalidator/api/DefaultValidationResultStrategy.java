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

/**
 * A default implementation of ValidationResultStrategy
 * @author deepak
 */
public class DefaultValidationResultStrategy implements ValidationResultStrategy
{
    private boolean isWarningEnabled;

    private boolean isErrorEnabled;

    private boolean stopAtFirstError;

    /**
     * Create a default strategy with both error and warnings enabled.
     * By default enableStopAtFirstError() is enabled as a default behaviour
     * so that Validation will be stop as soon as validator encounter first error
     */
    public DefaultValidationResultStrategy() {
        enableErrors();
        //enableWarnings();
        enableStopAtFirstError();
    }

    @Override
    public void enableWarnings() {
        this.isWarningEnabled = true;
    }

    @Override
    public void disableWarnings() {
        this.isWarningEnabled = false;
    }

    @Override
    public void enableErrors() {
        this.isErrorEnabled = true;
    }

    @Override
    public void disableErrors() {
        this.isErrorEnabled = false;
    }

    @Override
    public void enableStopAtFirstError() {
        this.stopAtFirstError = true;
    }

    @Override
    public void disableStopAtFirstError() {
        this.stopAtFirstError = false;
    }

    @Override
    public boolean isErrorEnabled() {
        return isErrorEnabled;
    }

    @Override
    public boolean isWarningEnabled() {
        return isWarningEnabled;
    }

    @Override
    public boolean shouldStopAtFirstError() {
        return stopAtFirstError;
    }

    /**
     * Return total warnings
     * @return total warnings
     */
    public int totalWarnings() {
        return 0;
    }

    /**
     * Return total errors
     * @return total errors
     */
    public int totalError() {
        return 0;
    }
}
