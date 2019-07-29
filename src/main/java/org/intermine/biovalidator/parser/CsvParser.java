package org.intermine.biovalidator.parser;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.ResultIterator;
import com.univocity.parsers.csv.CsvParserSettings;
import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ParsingException;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Parser for parsing CSV/TSV files
 * @author deepak kumar
 */
public class CsvParser implements Parser<String[]>
{
    private InputStreamReader inputStreamReader;
    private boolean hasHeader;
    private boolean allowComments;
    private boolean shouldAutoDetectDelimiter;
    private String delimiter;
    private com.univocity.parsers.csv.CsvParser csvParser;
    private ResultIterator<String[], ParsingContext> csvParserLineIterator;

    /**
     * Construct CsvParser with a input source and whether to allow comments or not
     * and by auto-detecting delimiter
     * @param inputStreamReader input source
     * @param hasHeader whether first line is a header line or not
     * @param allowComments whether to allow '#' based comments or not
     */
    public CsvParser(InputStreamReader inputStreamReader,
                     boolean hasHeader,
                     boolean allowComments) {
        this(inputStreamReader, hasHeader, allowComments, "");
    }

    /**
     * Construct CsvParser with a input source and a delimiter
     * @param inputStreamReader input source
     * @param hasHeader whether first line is a header line or not
     * @param allowComments whether to allow '#' based comments or not
     * @param delimiter delimiter to separate columns
     */
    public CsvParser(InputStreamReader inputStreamReader,
                     boolean hasHeader,
                     boolean allowComments,
                     String delimiter) {
        this.inputStreamReader = inputStreamReader;
        this.hasHeader = hasHeader;
        this.allowComments = allowComments;
        this.delimiter = delimiter;

        this.csvParser = createParser();
        this.csvParserLineIterator = csvParser.iterate(inputStreamReader).iterator();
    }

    private com.univocity.parsers.csv.CsvParser createParser() {
        CsvParserSettings settings = new CsvParserSettings();
        if (hasHeader) {
            settings.setNumberOfRowsToSkip(1); // skip header line if present
        }
        if (shouldAutoDetectDelimiter && delimiter.length() == 0) {
            settings.detectFormatAutomatically(); //if no delimiter provided
        } else {
            settings.getFormat().setDelimiter(delimiter);
        }
        return new com.univocity.parsers.csv.CsvParser(settings);
    }

    @Override
    public String[] parseNext() throws ParsingException {
        return csvParserLineIterator.next();
    }

    /**
     * Test whether next line available or not
     * @return boolean available or not
     */
    public boolean hasNext() {
        return csvParserLineIterator.hasNext();
    }

    @Override
    public void close() throws IOException {

    }
}
