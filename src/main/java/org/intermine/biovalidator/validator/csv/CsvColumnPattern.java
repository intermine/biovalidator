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


import java.util.Collections;
import java.util.List;

/**
 * Represents an immutable pattern of value in a csv column
 *Example:
 * Pattern : [DIGIT, LETTER, DIGIT]
 * @author deepak kumar
 */
public final class CsvColumnPattern
{
    /**
     * Represents maximum length of a pattern
     */
    public static final int MAX_PATTERN_LENGTH = 9;

    private String patternValue;

    private List<CsvColumnValueType> patternList;

    private static final CsvColumnPattern RANDOM_PATTERN =
            new CsvColumnPattern(Collections.singletonList(CsvColumnValueType.RANDOM));

    private CsvColumnPattern(List<CsvColumnValueType> patternList) {
        this.patternList = patternList;
    }

    /**
     * Construct an immutable column pattern for a csv column
     * @param patternList pattern list (list of value type that constructs a patter)
     * @return immutable instance of CsvColumnPattern
     */
    public static CsvColumnPattern of(List<CsvColumnValueType> patternList, String patternValue) {
        CsvColumnPattern pattern =  new CsvColumnPattern(patternList);
        pattern.patternValue = patternValue;
        return pattern;
    }

    /**
     * Create and return a pattern representing a random or mixed string pattern
     * @return pattern for a random or mixed string
     */
    public static CsvColumnPattern randomPattern() {
        return RANDOM_PATTERN;
    }

    @Override
    public boolean equals(Object thatObj) {
        if (this == thatObj) {
            return true;
        }
        if (thatObj == null) {
            return false;
        }
        if (!(thatObj instanceof CsvColumnPattern)) {
            return false;
        }
        CsvColumnPattern thatPattern = (CsvColumnPattern) thatObj;
        return this.patternList.equals(thatPattern.patternList);
    }

    @Override
    public int hashCode() {
        int result = 1;
        int val  = convertPatternToNumber(patternList);
        return 31 * result * val;
    }

    private int convertPatternToNumber(List<CsvColumnValueType> patternList) {
        int patternVal = 0;
        for (CsvColumnValueType valueType: patternList) {
            patternVal = (patternVal * 10) + valueType.getId();
        }
        return patternVal;
    }

    @Override
    public String toString() {
        return patternList.toString();
    }
    /**
     * Gets pattern list
     * @return list of pattern of a column
     */
    public List<CsvColumnValueType> getPatternList() {
        return patternList;
    }
}
