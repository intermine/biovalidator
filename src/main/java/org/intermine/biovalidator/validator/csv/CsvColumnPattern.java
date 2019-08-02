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
            new CsvColumnPattern(String.valueOf(CsvColumnValueType.RANDOM.getId()), null);

    private CsvColumnPattern(String pattern, String data) {
        this.pattern = pattern;
        this.data = data;
    }


    /**
     * Create a pattern fom given string data, if created pattern are duplicated consecutively
     * then it will replace multiple occurrence of same pattern with one(i.e compress patterns)
     * @param data data from which pattern has to created
     * @return immutable instance of CsvColumnPattern
     */
    public static CsvColumnPattern of(String data) {
        String pattern = PatternUtils.createCsvColumnPatternFromString(data);
        return new CsvColumnPattern(PatternUtils.compressRepeatedPatterns(pattern), data);
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
