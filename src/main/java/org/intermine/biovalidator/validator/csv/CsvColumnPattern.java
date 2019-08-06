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


import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Represents an immutable pattern of value of a csv-column
 * Example:
 *      "abc"        = PATTERN [LETTERS]
 *      "abs123"     = PATTERN [LETTERS, DIGITS]
 *      "SO:12736"   = PATTERN [LETTERS, SYMBOL, DIGITS]
 * @author deepak kumar
 */
public final class CsvColumnPattern
{
    /**
     * Represents maximum length of a pattern
     */
    public static final int MAX_PATTERN_LENGTH = 30;

    /**
     * Represents manimum length of data will be stored by the pattern
     */
    public static final int MAX_PATTERN_DATA_LENGTH = 30;

    private static final String EXTRA_STRING = "...";

    private String data;
    private String pattern;

    private static final CsvColumnPattern RANDOM_PATTERN =
        new CsvColumnPattern(String.valueOf(CsvColumnValueType.RANDOM.getId()), StringUtils.EMPTY);

    private static final CsvColumnPattern EMPTY_PATTERN =
        new CsvColumnPattern(StringUtils.EMPTY, StringUtils.EMPTY);

    private static final CsvColumnPattern DIGIT_PATTERN =
            new CsvColumnPattern("1233", "123");

    private CsvColumnPattern(String pattern, String data) {
        this.pattern = pattern;

        // store data(i.e. column-value from pattern is created) up to MAX_PATTERN_LENGTH
        if (data != null) {
            if (data.length() <= MAX_PATTERN_LENGTH) {
                this.data = data;
            } else {
                this.data = data.substring(0, 30) + EXTRA_STRING;
            }
        }
    }

    /**
     * Create a pattern fom given string data, if created pattern has consecutively duplicated
     * pattern or text, then it will replace multiple occurrence of same pattern with one
     * (i.e compress patterns)
     * @param data data from which pattern has to created
     * @return immutable instance of CsvColumnPattern
     */
    public static CsvColumnPattern valueOf(String data) {
        if (data == null) {
            return emptyPattern();
        }
        String pattern = PatternUtils.createCsvColumnPatternFromString(data);
        String compressedPattern = PatternUtils.compressRepeatedPatterns(pattern);
        if (compressedPattern != null && compressedPattern.length() > MAX_PATTERN_LENGTH) {
            return randomPattern();
        }
        return new CsvColumnPattern(compressedPattern, data);
    }

    /**
     * Create a pattern fom given string data without any compression
     * @param data data from which pattern has to created
     * @return immutable instance of CsvColumnPattern
     */
    public static CsvColumnPattern withoutCompression(String data) {
        return new CsvColumnPattern(PatternUtils.createCsvColumnPatternFromString(data), data);
    }

    /**
     * Create and return a pattern representing a random or mixed string pattern
     * @return pattern for a random or mixed string
     */
    public static CsvColumnPattern randomPattern() {
        return RANDOM_PATTERN;
    }

    /**
     * Create and return a empty pattern
     * @return pattern for a empty string
     */
    public static CsvColumnPattern emptyPattern() {
        return EMPTY_PATTERN;
    }

    /**
     * Create and return a pattern consist of digits
     * @return pattern for a integer string
     */
    public static CsvColumnPattern digitattern() {
        return DIGIT_PATTERN;
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

    @Override
    public String toString() {
        return pattern;
    }

    /**
     * Gets pattern list
     * @return list of pattern of a column
     */
    public List<CsvColumnValueType> getPatternList() {
        return PatternUtils.deSerializePatternList(pattern);
    }

    /**
     * Returns data from pattern has been created
     * @return data of the pattern
     */
    public String getData() {
        return data;
    }

    /**
     * Gets pattern
     * @return pattern
     */
    public String getPattern() {
        return pattern;
    }
}
