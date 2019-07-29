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
import org.intermine.biovalidator.api.ParsingException;
import org.intermine.biovalidator.parser.CsvParser;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author deepak kumar
 */
public class CsvHeaderDetector
{
    private static final int SAMPLE_ROW = 10;
    private InputStreamReader inputStreamReader;
    private String delimiter;

    /**
     * Create a constructor with input stream and by auto-detecting delimiter type
     * @param inputStreamReader input-stream to the file
     */
    public CsvHeaderDetector(InputStreamReader inputStreamReader) {
        this(inputStreamReader, "");
    }

    /**
     * input stream
     * @param inputStreamReader input stream to the file
     * @param delimiter delimiter for csv/tsv file
     */
    public CsvHeaderDetector(InputStreamReader inputStreamReader, String delimiter) {
        this.inputStreamReader = inputStreamReader;
        this.delimiter = delimiter;
    }


    /**
     * test whether a csv data has a header or not
     * @throws ParsingException if failed
     * @return boolean
     */
    public boolean hasHeader() throws ParsingException {
        CsvParser parser = new CsvParser(inputStreamReader, true, delimiter);

        String[] header = parser.parseNext();
        int totalColumns = header.length;

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

            columnTypes.forEach((colKeyIndex, colVal) -> {
                if (NumberUtils.isCreatable(row[colKeyIndex])) {
                    if (!(colVal instanceof Boolean)) {
                        if (colVal == null) {
                            columnTypes.put(colKeyIndex, true);
                        } else {
                            columnTypes.remove(colKeyIndex);
                        }
                    }
                } else {
                    columnTypes.put(colKeyIndex, row[colKeyIndex].length());
                }
            });
            checked++;
        }
        int hasHeader = 0;
        for (Map.Entry<Integer, Object> entry: columnTypes.entrySet()) {
            int colKey = entry.getKey();
            Object colVal = entry.getValue();
            if (colVal instanceof Integer) {
                int colValInt = (Integer) colVal;
                if (header[colKey].length() == colValInt) {
                    hasHeader--;
                } else {
                    hasHeader++;
                }
            } else if (colVal instanceof Boolean) {
                if (NumberUtils.isCreatable(header[colKey])) {
                    hasHeader--;
                } else {
                    hasHeader++;
                }
            }
        }
        return hasHeader > 0;
    }
}
