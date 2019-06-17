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
 * Represents an immutable Message that can be created by validator whenever
 * validator finds an invalid data.
 *
 * @author deepak
 */
public abstract class Message
{
    private String message;

    /**
     * Construct a message with given string
     * @param message message string
     */
    public Message(String message) {
        this.message = message;
    }

    /**
     * Gets message.
     *
     * @return Value of message.
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
