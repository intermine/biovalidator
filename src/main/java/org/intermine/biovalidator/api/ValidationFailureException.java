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
 * @author deepak
 */
public class ValidationFailureException extends Exception
{
    /**
     * Construct a ValidationFailue Exception with a message as the cause.
     * @param msg the reason for failure.
     */
    public ValidationFailureException(String msg) {
        super(msg);
    }
}
