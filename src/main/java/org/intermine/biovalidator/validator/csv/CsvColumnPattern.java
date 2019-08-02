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

    private String data;
    private String pattern;

    private static final CsvColumnPattern RANDOM_PATTERN =
            of(Collections.singletonList(CsvColumnValueType.RANDOM), null);

    private CsvColumnPattern(String pattern, String data) {
        this.pattern = pattern;
        this.data = data;
    }

    /**
     * Construct an immutable column pattern for a csv column
     * @param patternList pattern list (list of value type that constructs a patter)
     * @param patternData data
     * @return immutable instance of CsvColumnPattern
     */
    public static CsvColumnPattern of(List<CsvColumnValueType> patternList, String patternData) {
        String serializedPattern = PatternUtils.serializePatternList(patternList);
        //String compressedStringPattern = PatternUtils.compressRepeatedPatterns(serializedPattern);
        return new CsvColumnPattern(serializedPattern, patternData);
    }

    /**
     * Create a pattern fom given string data
     * @param data data from which pattern has to created
     * @return immutable instance of CsvColumnPattern
     */
    public static CsvColumnPattern of(String data) {
        return PatternUtils.createCsvColumnPatternFromString(data);
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
        return this.pattern.equals(thatPattern.pattern);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
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
        return pattern;
    }

    /**
     * Gets pattern list
     * @return list of pattern of a column
     */
    public List<CsvColumnValueType> getPatternList() {
        return null;
    }
}
