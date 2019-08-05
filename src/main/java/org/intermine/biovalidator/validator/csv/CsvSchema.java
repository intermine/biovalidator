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
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents schema of cvs file/data, meaning it stores information about columns of csv data,
 * like the number of columns exists, type of each column(single-type/mixed-type), and patterns
 * of data of columns.
 *
 * It two kind of information about a column:
 * 1. Total number of booleans, integer and float counts
 * 2. Create patterns from column data and count their occurrence
 *
 * <strong>PATTERN CREATION PROCESS</strong>
 * In Order to detect pattern and measuring consistency, A pattern is defined for each column-value
 * based on the data, and then putting all the column value that conforms to one pattern into
 * one bucket, so one bucket for each pattern, then at the end analyze that all the buckets have
 * roughly equal amount of data or not.
 *
 * Process For Pattern CREATION:
 * Value of a column into five types: ENUM [DIGITS, LETTERS, SYMBOLS, SPACES, RANDOM].
 * then, for each column value, A pattern(which is a list defined ENUM above) is created based on
 * the data of column value.
 *
 * Example of pattern creation:
 * ------------------------------------------------------------------
 * "abc"             = PATTERN [LETTERS]
 * "abs123"          = PATTERN [LETTERS, DIGITS]
 * "SO:12736"        = PATTERN [LETTERS, SYMBOL, DIGITS]
 * "region-1029-abc" = PATTERN [LETTERS, SYMBOL, DIGITS, LETTERS]
 * etc...
 * ------------------------------------------------------------------
 *
 * On the implementation side, InOrder to divide these pattern into different buckets, hash value
 * of each pattern is calculated, and then put all of them into a hashtable and count the
 * occurrence of each pattern.
 *
 * As a column can have many pattern so, few constraints followed by this class is:
 * 1. Each pattern is compressed:
 *    If a pattern has similar pattern-value then all the consecutively duplicated patterns are
 *    removed, ex: 'LDLDLD' is compressed to 'LD' (consecutively duplicated are removed).
 *
 * 2. If two patterns are almost similar then are merged into one pattern, currently in-order
 *    <a href="https://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance">'Jaro–Winkler distance'
 *    </a> is used to detect similarity between two patterns.
 *
 * 3. If a created pattern is longer that 30 characters(after compression) then it is assumed that
 *    pattern is random.
 *
 * 4. If a column has more that 30 patterns then it assumed that column has some random data.
 *
 * @author deepak kumar
 */
public class CsvSchema implements Iterable<CsvColumnMatrics>
{
    private int columnLength;
    private long totalRows;
    private List<CsvColumnMatrics> columns; //stores matrics for each column

    /**
     * Construct an empty schema with specified number of columns
     * @param columnLength length of total columns
     */
    public CsvSchema(int columnLength) {
        this.columnLength = columnLength;
        initColumns();
    }

    /**
     * Increments booleans count at a given column index
     * Note: assumes given column index is a valid index
     * @param i index of columns
     */
    public void incrementBooleansCountAtColumn(int i) {
        colAt(i).incrementBooleanCount();
    }

    /**
     * Increments integers count at a given column index
     * Note: assumes given column index is a valid index
     * @param i index of columns
     */
    public void incrementIntegersCountAtColumn(int i) {
        colAt(i).incrementIntegerCount();
    }

    /**
     * Increments floats count at a given column index
     * Note: assumes given column index is a valid index
     * @param i index of columns
     */
    public void incrementFloatsCountAtColumn(int i) {
        colAt(i).incrementFloatsCount();
    }

    /**
     * initialize columns data with default values
     */
    private void initColumns() {
        this.columns = new ArrayList<>(columnLength);
        for (int i = 0; i < columnLength; i++) {
            columns.add(new CsvColumnMatrics());
        }
    }

    /**
     * Gets a column at a given index,
     * @param i index of the column
     * @return instance of CsvColumnMatrics at a given index
     */
    public CsvColumnMatrics colAt(int i) {
        if (i < 0 || i >= columns.size()) {
            throw new IndexOutOfBoundsException("column index out of bounds!");
        }
        return columns.get(i);
    }

    /**
     * Gets length of columns
     * @return column length
     */
    public int getColumnLength() {
        return columnLength;
    }

    @Override
    public Iterator<CsvColumnMatrics> iterator() {
        return columns.iterator();
    }

    @Override
    public void forEach(Consumer<? super CsvColumnMatrics> consumer) {
        for (CsvColumnMatrics column: columns) {
            consumer.accept(column);
        }
    }

    /**
     * Gets total rows
     * @return row total
     */
    public long getTotalRows() {
        return totalRows;
    }

    /**
     * Sets row total
     * @param totalRows total rows
     */
    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }
}
