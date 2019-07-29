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
     * test whether a csv data has a header or not
     * @throws ParsingException if failed
     * @return boolean
     */
    public boolean hasHeader() throws ParsingException {
        /*
         tell the parse that file does not has header so it won't skip first line, as this
         class wants to read first line in order to detect whether file has header or not.
         So pass hasHeader=false in the CsvParser() constructor
          */
        CsvParser parser = new CsvParser(inputStreamReader, false, allowComments, delimiter);

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

            for (int i = 0; i < totalColumns; i++) {
                if (NumberUtils.isCreatable(row[i])) {
                    if (!(columnTypes.get(i) instanceof Boolean)) {
                        if (columnTypes.get(i) == null) {
                            columnTypes.put(i, true);
                        } else {
                            columnTypes.remove(i);
                        }
                    }
                } else {
                    if (row[i] == null) {
                        columnTypes.put(i, 0);
                    } else {
                        columnTypes.put(i, row[i].length());
                    }

                }
            }
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
