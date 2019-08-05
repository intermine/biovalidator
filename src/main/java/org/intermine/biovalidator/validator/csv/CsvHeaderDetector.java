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
 *      See original python's implementation <a href="https://bit.ly/2LYHR6l"> here </a>
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
     * port of python csv module's 'has_header()' method, see link to the code at the top of the
     * file for description about approach being uses here.
     *
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
                    if (NumberUtils.isCreatable(row[i])) {
                        thisType = Boolean.TRUE;
                    } else {
                        thisType = row[i] == null ? 0 : row[i].length();
                    }
                    if (thisType != columnTypes.get(i)) {
                        if (columnTypes.get(i) == null) {
                            columnTypes.put(i, thisType);
                        } else {
                            columnTypes.remove(i);
                        }
                    }
                });
                checked++;
            }
            int hasHeader = 0;
            for (Map.Entry<Integer, Object> entry : columnTypes.entrySet()) {
                int colKey = entry.getKey();
                Object colVal = entry.getValue();
                if (colVal instanceof Integer) {
                    int colValInt = (Integer) colVal;
                    if (header[colKey] != null && header[colKey].length() == colValInt) {
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
