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

/**
 * Represents an immutable warning message
 * @author deepak
 */
public class WarningMessage extends Message
{
    /**
     * Construct a message with given string
     *
     * @param message message string
     */
    public WarningMessage(String message) {
        super(message);
    }

    /**
     * createa an error message from string
     * @param msg error message
     * @return instance of error message
     */
    public static WarningMessage of(String msg) {
        return new WarningMessage(msg);
    }
}
