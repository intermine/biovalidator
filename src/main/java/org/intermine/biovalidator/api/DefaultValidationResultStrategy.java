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
 * @author deepak
 */
public class DefaultValidationResultStrategy implements ValidationResultStrategy
{
    private boolean enableWarning;

    private boolean enableError;

    private boolean constructDetailedMessage;

    private boolean stopAtFirstError;

    @Override
    public void enableWarnings() {
        this.enableError = true;
    }

    @Override
    public void disableWarnings() {
        this.enableError = false;
    }

    @Override
    public void enableErrors() {
        this.enableError = true;
    }

    @Override
    public void disableErrors() {
        this.enableError = false;
    }

    @Override
    public void stopAtFirstError() {
        this.stopAtFirstError = true;
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
