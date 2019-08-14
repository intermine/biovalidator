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
    DIGITS('D'),

    /**
     * Represents one or more letters in a column value
     * Examples:
     * 1. abc
     * 2. biovalidator
     */
    LETTERS('L'),

    /**
     * Represents one or more symbols in a column value
     * Examples:
     * 1. symbols like '-',  '!', '@', '#', '$', '%', '^', '{@literal &}', '*', '(', ')', '+', '-'
     */
    SYMBOLS('S'),

    /**
     * Represents one or more spaces in a column value
     * Examples:
     * 1. any whitespace like ' ', '\t', '   ', '\r', '\n', etc..
     */
    SPACES(' '),

    /**
     * Represents a random type in a column value
     */
    RANDOM('R');

    private char id;

    /**
     * Construct a csv column value type with its ID
     * @param id id of the type
     */
    CsvColumnValueType(char id) {
        this.id = id;
    }

    /**
     * Gets id of the value type
     * @return id of the value type
     */
    public char getId() {
        return id;
    }

    /**
     * returns type of a character
     * @param c input character
     * @return matching CsvColumnValueType of the character
     */
    public static CsvColumnValueType getType(char c) {
        if (Character.isDigit(c)) {
            return CsvColumnValueType.DIGITS;
        } else if (Character.isLetter(c) || Character.isWhitespace(c)) {
            return CsvColumnValueType.LETTERS;
        } else {
            return CsvColumnValueType.SYMBOLS;
        }
    }

    /**
     * Create and return type from char id
     * @param c id
     * @return equivalent type
     */
    public static CsvColumnValueType of(char c) {
        for (CsvColumnValueType type : CsvColumnValueType.values()) {
            if (type.getId() == c) {
                return type;
            }
        }
        throw new IllegalArgumentException("unable to find type of pattern");
    }
}
