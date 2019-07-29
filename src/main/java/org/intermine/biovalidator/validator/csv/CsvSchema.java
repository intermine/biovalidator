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
 * Represents schema of cvs file/data, meaning it stores information of columns of csv data,
 * like the number of columns exists, type of each column(single-type/mixed-type), and patterns
 * of data of columns
 * @author deepak kumar
 */
public class CsvSchema implements Iterable<CsvColumnMatrics>
{
    private int columnLength;
    private long totalRows;
    private List<CsvColumnMatrics> columns;

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
     * Note: assumes given column index is a valid index
     * @param i index of the column
     * @return instance of CsvColumnMatrics at a given index
     */
    public CsvColumnMatrics colAt(int i) {
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
