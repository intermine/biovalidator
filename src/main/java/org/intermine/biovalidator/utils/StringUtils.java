package org.intermine.biovalidator.utils;

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
 * Utility functions for working with String
 * @author deepak
 */
public final class StringUtils
{
    private StringUtils() { }
    /**
     * Test whether given string is blank or not
     * @param str string to be tested
     * @return whethet true if string is empty else false
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().isEmpty();
    }

    /**
     * Test whether string is non-null and non-empty or not
     * @param str to be tested
     * @return either true or false
     */
    public static boolean nonNullNonEpty(String str) {
        return !isBlank(str);
    }
}
