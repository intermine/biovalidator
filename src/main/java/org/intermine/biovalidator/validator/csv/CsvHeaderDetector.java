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

import org.apache.commons.lang3.math.NumberUtils;
import org.intermine.biovalidator.parser.CsvParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This header detector is a port of python standard library csv module (csv.Sniffer.has_header)'.
 *  <pre>
 *      See original python's implementation <a href="http://bit.ly/2H4ndNN" target="_blank">
 *          here </a>
 *  </pre>
 * @author deepak kumar
 */
public class CsvHeaderDetector
{
    private static final int SAMPLE_ROW = 15;
    private InputStreamReader inputStreamReader;
    private boolean allowComments;
    private String delimiter;

    /**
     * input stream
     * @param inputStreamReader filepath
     * @param allowComments allow comments preceded with '#' or not
     * @param delimiter delimiter for csv/tsv file
     */
    public CsvHeaderDetector(InputStreamReader inputStreamReader,
                             boolean allowComments,
                             String delimiter) {
        this.inputStreamReader = inputStreamReader;
        this.allowComments = allowComments;
        this.delimiter = delimiter;
    }


    /**
     * Test whether a csv data has a header or not.
     * This is a Java port of python csv module's 'has_header()' method, see link to the code
     * at the top of the file for description about approach used in the original implementation.
     *
     * Approach for detecting header:
     * <p>
     * 1. It looks for a column which is of a single type(say integer) and if all rows have the
     * same type of value except the first row, then the first row is considered as a header.
     *
     * 2. If the first check doesn't work, then it also counts the length of the value of each
     * column, if all values of a column have the same length except the first row, then again
     * its a header.
     * </p>
     * <p>
     *     See original python implementation: <a href="http://bit.ly/2H4ndNN" target="_blank">
     *         here</a>
     * </p>
     * @throws IOException if failed parsing
     * @return boolean
     */
    public boolean hasHeader() throws IOException {
        /*
         Tell the parse that file does not has header so it won't skip first line, as this
         class wants to read first line in order to detect whether file has header or not.
         So passing hasHeader=false in the CsvParser() constructor
          */
        try (CsvParser parser = new CsvParser(inputStreamReader, false, allowComments, delimiter)) {
            String[] header = parser.parseNext();
            int totalColumns = header.length;

            /*
             This map stores entry for each column, for each column-mapping it stores column index
             as key, and an object representing value, value can be one of two possible objects:
             1. Boolean object:
                If a value is of Boolean type, then it means up to now all rows are number for a
                particular column.
             2. Integer objects:
                If a value is of Integer type, then it means that the column does not have all
                value as number but rather string, so now it will store length of column-values.
             */
            Map<Integer, Object> columnTypes = new HashMap<>();

            for (int i = 0; i < totalColumns; i++) {
                columnTypes.put(i, null);
            }
            int checked = 0;
            while (parser.hasNext() && checked < SAMPLE_ROW) {
                String[] row = parser.parseNext();

                if (row.length != totalColumns) {
                    continue;
                }
                //Copy keys to avoid Modification Exception
                Set<Integer> columnTypesKeys = new HashSet<>(columnTypes.keySet());
                columnTypesKeys.forEach(i -> {
                    Object thisType;
                    if (NumberUtils.isCreatable(row[i])) { //if column-value is a number
                        thisType = Boolean.TRUE; // true indicating column-value is of number type
                    } else {
                        // if a column-value isn't a number, store it's length
                        thisType = row[i] == null ? 0 : row[i].length();
                    }
                    if (thisType != columnTypes.get(i)) {
                        if (columnTypes.get(i) == null) {
                            columnTypes.put(i, thisType); // add new column type
                        } else {
                            // type is inconsistent, remove column from consideration
                            columnTypes.remove(i);
                        }
                    }
                });
                checked++;
            }

            /*
             Now check whether there is a column which is either all number except first row, OR
             there is a column whose length of all values are same except the first row,

             Finally, compare results against first row and "vote" on whether it's a header.
             */
            int hasHeader = 0;
            for (Map.Entry<Integer, Object> entry : columnTypes.entrySet()) {
                int colKey = entry.getKey();
                Object colVal = entry.getValue();
                if (colVal instanceof Integer) { // Integer means, considering length of col-values
                    int colValInt = (Integer) colVal;
                    if (header[colKey] != null && header[colKey].length() == colValInt) {
                        // If length of first row is same as all other rows,
                        // then it might not be a header, decrement vote by 1
                        hasHeader--;
                    } else {
                        hasHeader++; // else it might be a header, So increment vote by 1
                    }
                } else if (colVal instanceof Boolean) {
                    // If column-value is a Booleans then, all rows(except first row) are numbers
                    if (NumberUtils.isCreatable(header[colKey])) {
                        // if first row is also a number then, it may not be a header
                        hasHeader--;
                    } else {
                        hasHeader++; // else it might be a header, So increment vote by 1
                    }
                }
            }
            return hasHeader > 0;
        }
    }
}

/*
var sample = [
    ['Usern',2323,'First_name', 'Last_Name'],
    ['booker12',9012,'Rachel', 'Booker'],
    ['grey07',2070,'Laura', 'Grey'],
    ['johnson81',4081,'Craig', 'Johnson'],
    ['jenkins46',9346,'Mary', 'Jenkins'],
    ['smith79',5079,'Jamie', 'Smith']
];

 */
