package org.intermine.biovalidator.validator.csv;

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
 * Represents type of possible values in csv column value
 *
 * @author deepak kumar
 */

public enum CsvColumnValueType
{
    /**
     * Represents one or more digits in a column value
     * Examples:
     * 1. 12323
     * 2. 00231234
     */
    DIGITS((short) 1),

    /**
     * Represents one or more letters in a column value
     * Examples:
     * 1. abc
     * 2. biovalidator
     */
    LETTERS((short) 2),

    /**
     * Represents one or more symbols in a column value
     * Examples:
     * 1. symbols like '-',  '!', '@', '#', '$', '%', '^', '{@literal &}', '*', '(', ')', '+', '-'
     */
    SYMBOLS((short) 3),

    /**
     * Represents one or more spaces in a column value
     * Examples:
     * 1. any whitespace like ' ', '\t', '   ', '\r', '\n', etc..
     */
    SPACES((short) 4),

    /**
     * Represents a random type in a column value
     */
    RANDOM((short) 4);

    private short id;

    /**
     * Construct a csv column value type with its ID
     * @param id id of the type
     */
    CsvColumnValueType(short id) {
        this.id = id;
    }

    /**
     * Gets id of the value type
     * @return id of the value type
     */
    public short getId() {
        return id;
    }
}
