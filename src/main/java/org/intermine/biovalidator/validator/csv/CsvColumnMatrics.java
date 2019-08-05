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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents metrics of a columns of a csv file.
 * For each column value it count total number of booleans, integer, floats and patterns.
 * @author deepak kumar
 */
public class CsvColumnMatrics
{
    /**
     * Represents maximum number of patterns allowed in a column
     */
    public static final int MAX_ALLOWED_PATTERNS_IN_A_COLUMN = 30;
    private long booleansCount;
    private long integersCount;
    private long floatsCount;

    private boolean isMaxPatternAllowedExceed;

    private Map<CsvColumnPattern, Integer> columnDataPatternsWithOccurrenceCount;

    /**
     * Construct a CsvColumnMatrics instance
     */
    public CsvColumnMatrics() {
        this.columnDataPatternsWithOccurrenceCount = new HashMap<>();
    }

    /**
     * Add a column pattern with zero as value, if pattern already exist then increment its count
     * @param pattern instance of CsvColumnPattern
     * @return returns added or not
     */
    public boolean addPattern(CsvColumnPattern pattern) {
        if (columnDataPatternsWithOccurrenceCount.size() >= MAX_ALLOWED_PATTERNS_IN_A_COLUMN) {
            isMaxPatternAllowedExceed = true;
        }
        if (!isMaxPatternAllowedExceed) {
            Integer count = columnDataPatternsWithOccurrenceCount.get(pattern);
            if (count == null) {
                count = 1;
            } else {
                count++;
            }
            this.columnDataPatternsWithOccurrenceCount.put(pattern, count);
            return true; // TODO return exactly what happened
        } else {
            return false;
        }
    }

    /**
     * increments count booleans by 1
     */
    public void incrementBooleanCount() {
        booleansCount++;
    }

    /**
     * increments count booleans by 1
     */
    public void incrementIntegerCount() {
        integersCount++;
    }

    /**
     * increments count booleans by 1
     */
    public void incrementFloatsCount() {
        floatsCount++;
    }

    /**
     * Gets booleansCount
     * @return current count of booleans
     */
    public long getBooleansCount() {
        return booleansCount;
    }

    /**
     * Gets integersCount
     * @return current count of integers
     */
    public long getIntegersCount() {
        return integersCount;
    }

    /**
     * Gets booleansCount
     * @return current count of floats
     */
    public long getFloatsCount() {
        return floatsCount;
    }

    /**
     * Gets all data patterns of the column
     * @return data patterns
     */
    public Map<CsvColumnPattern, Integer> getColumnDataPatterns() {
        return columnDataPatternsWithOccurrenceCount;
    }

    /**
     * Gets isMaxPatternAllowedExceed
     * @return boolean, representing whether column has more than allowed pattern or not
     */
    public boolean isMaxPatternAllowedExceed() {
        return isMaxPatternAllowedExceed;
    }
}
