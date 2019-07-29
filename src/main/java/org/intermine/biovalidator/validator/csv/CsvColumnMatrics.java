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
 * Represents metrics of a columns of a csv file
 *
 * @author deepak kumar
 */
public class CsvColumnMatrics
{
    private long booleansCount;
    private long integersCount;
    private long floatsCount;

    private Map<CsvColumnPattern, Integer> columnPatternsMappingWithValueCount;

    /**
     * Construct a CsvColumnMatrics instance
     */
    public CsvColumnMatrics() {
        this.columnPatternsMappingWithValueCount = new HashMap<>();
    }

    /**
     * Add a column pattern with zero as value, if pattern already exist then increment its count
     * @param pattern instance of CsvColumnPattern
     * @return returns added or not
     */
    public boolean addColumnPattern(CsvColumnPattern pattern) {
        Integer count = columnPatternsMappingWithValueCount.get(pattern);
        if (count == null) {
            count = 0;
        } else {
            count++;
        }
        this.columnPatternsMappingWithValueCount.put(pattern, count);
        return true; // TODO return exactly what happened
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
}
