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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utilities for creating and manipulating csv column-value patterns
 * @author deepak kumar
 */
public final class PatternUtils
{
    private static final Pattern COMPRESS_PATTERN = Pattern.compile("(.+?)\\1+");
    private PatternUtils() { }

    /**
     * Create a pattern for a given string
     * Examples:
     *     1. "abc"             -{@literal >}  [LETTERS]
     *     2. "bio101"          -{@literal >}  [LETTERS, DIGITS]
     *     3. "SO:3347"         -{@literal >}  [LETTERS, SYMBOL, DIGITS]
     *     4. "amino-acid (RNA)"-{@literal >}  [LETTERS] symbols between text(string)
     *     are not considered
     * @param s a string
     * @return CsvColumnPattern
     */
    public static String createCsvColumnPatternFromString(String s) {
        StringBuilder columnValuePattern = new StringBuilder();
        int len = s.length();
        CsvColumnValueType prevType = null;
        for (int i = 0; i < len; i++) {
            CsvColumnValueType currentType = CsvColumnValueType.getType(s.charAt(i));
            //skip while type is same
            while ((i + 1) < len && currentType == CsvColumnValueType.getType(s.charAt(i + 1))) {
                i++;
            }

            /*
                skip commonly used text symbols (such as ',', '(', ')', etc), if it is
                between two string text.
                Example where symbols will be skipped:
                    1. "Hi, deer"           -> skip ','
                    2. "United State(US)"   -> skip '(', ')' as both are part of text
                    3. "region & mRNA"      -> skip '&'
                    4. "1234-abc"           -> '-' won't be skipped, because it's not between
                                               two string
             */
            if ((i + 1) < len && prevType != null
                    && isGenerallyAcceptedStringSymbol(s.charAt(i))
                    && prevType == CsvColumnValueType.LETTERS
                    && CsvColumnValueType.getType(s.charAt(i + 1)) == CsvColumnValueType.LETTERS) {
                continue;
            }
            if ((i + 1) == len && (prevType == CsvColumnValueType.LETTERS)
                    && currentType == CsvColumnValueType.SYMBOLS) {
                continue;
            } else {
                if (currentType != prevType) {
                    columnValuePattern.append(currentType.getId());
                }
            }
            prevType = currentType;
        }
        // if list of pattern in a single column value is greater than defined MAX pattern length,
        // then it is assumed that, the given column value has a random or very mixed pattern
        /*if (columnValuePattern.size() > CsvColumnPattern.MAX_PATTERN_LENGTH) {
            return CsvColumnPattern.randomPattern();
        }*/
        return columnValuePattern.toString();
    }

    private static boolean isGenerallyAcceptedStringSymbol(char c) {
        return c == ',' || c == '(' || c == ')' || c == '\'' || c == '\"' || c == '&' || c == '_';
    }

    /**
     * Serialize a list of CsvColumnValueType(representing a pattern) into a string
     * @param pattern list CsvColumnValueType
     * @return serialized string version of the pattern
     */
    public static String serializePatternList(List<CsvColumnValueType> pattern) {
        StringBuilder serializedPatternStr = new StringBuilder();
        for (CsvColumnValueType patternType: pattern) {
            serializedPatternStr.append(patternType.getId());
        }
        return serializedPatternStr.toString();
    }

    /**
     * De-Serialize a serialized string pattern into a list of list of
     * CsvColumnValueType(representing a pattern)
     * @param serializedPattern serialized pattern string
     * @return list of CsvColumnValueType(representing a pattern)
     */
    public static List<CsvColumnValueType> deSerializePatternList(String serializedPattern) {
        int len = serializedPattern.length();
        List<CsvColumnValueType> pattern = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            pattern.add(CsvColumnValueType.of(serializedPattern.charAt(i)));
        }
        return pattern;
    }

    /**
     * Replace repeated patterns in string to single occurrence, it replace consecutive repeated
     * pattern in a string to its single occurrence
     * Example :
     *  1. "AAAAAAB"        -{@literal >} "AB"
     *  2. "ABAB"           -{@literal >} AB
     *  3. "ABCXYZABCXYZ"   -{@literal >} ABCXYZ
     * @param patternStr string to compress
     * @return compressed string with replaced repeated pattern with single occurrence
     */
    public static String compressRepeatedPatterns(String patternStr) {
        //return patternStr.replaceAll("(.+?)\\1+", "$1");
        return COMPRESS_PATTERN.matcher(patternStr).replaceAll("$1");
    }
}
