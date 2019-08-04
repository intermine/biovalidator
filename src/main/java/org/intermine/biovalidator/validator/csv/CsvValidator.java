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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.intermine.biovalidator.api.ParsingException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.parser.CsvParser;
import org.intermine.biovalidator.validator.AbstractValidator;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * Validator for validating csv/tsv data
 * @author deepak kumar
 */
public class CsvValidator extends AbstractValidator
{
    private static final String BOOLEAN_TRUE = "true";
    private static final String BOOLEAN_FALSE = "false";
    private static final double SUCCESS_SCORE_BAR = 0.5;
    private File file;
    private boolean allowComments;
    private boolean shouldAutoDetectDelimiter;
    private String delimiter;

    /**
     * Construct CsvParser with a input source by auto-detecting delimiter
     * and with allowed '#' based comments
     *
     * @param file input source
     */
    public CsvValidator(File file) {
        this(file, true);
    }

    /**
     * Construct CsvParser with a input source and whether to allow comments or not
     * and by auto-detecting delimiter
     * @param file input file
     * @param allowComments whether to allow '#' based comments or not
     */
    public CsvValidator(File file, boolean allowComments) {
        this(file, allowComments, "");
    }

    /**
     * Construct CsvParser with a input source and whether to allow comments or not
     * and by auto-detecting delimiter
     * @param file input file
     * @param allowComments whether to allow '#' based comments or not
     * @param delimiter delimiter for column separator
     */
    public CsvValidator(File file, boolean allowComments, String delimiter) {
        this.file = file;
        this.allowComments = allowComments;
        if (StringUtils.isNotBlank(delimiter)) {
            this.delimiter = delimiter;
        } else {
            this.shouldAutoDetectDelimiter = true;
        }
    }

    /**
     * <p>Validates a csv/tsv data</p>
     * <p>
     *   this method build a type schema for each file's column,
     *   InOrder to check consistency of each column this CsvValidator performs two type of Checks:
     *   1. Check whether column is of single type or not (ex. integer, boolean, etc)
     *   2. If column's data does not have a single type value then it creates patterns from the,
     *      data and test whether data is evenly distributes among found patterns or not.
     * </p>
     * @return validation result
     */
    @Nonnull
    @Override
    public ValidationResult validate() {
        try {
            CsvSchema csvSchema = null; // stores type and pattern information of csv column data
            boolean doesFileHasHeader = guessFileHasHeaderOrNot();

            InputStreamReader inputStreamToFile = createInputStreamFrom(file);
            CsvParser csvParser = new CsvParser(
                    inputStreamToFile, doesFileHasHeader, allowComments, delimiter);

            int columnsLength = 0;
            long currentLineNum = 0;

            while (csvParser.hasNext()) {
                currentLineNum++;
                String[] currentRow = csvParser.parseNext();

                if (currentLineNum <= 1) {
                    //init column information
                    columnsLength = currentRow.length;
                    csvSchema = new CsvSchema(columnsLength);
                }
                //check number of column is same or not
                if (currentRow.length != columnsLength) {
                    String warningMsg = "Wrong number of columns at line " + currentLineNum;
                    validationResult.addError(warningMsg);
                    if (validationResult.isNotValid()
                            && validationResultStrategy.shouldStopAtFirstError()) {
                        return validationResult;
                    }
                }

                //check consistency of each column of current row with each column of the first row
                for (int colIndx = 0; colIndx < columnsLength; colIndx++) {
                    String currentColVal = currentRow[colIndx];
                    if (StringUtils.isBlank(currentColVal)) {
                        validationResult.addWarning("column " + colIndx + " at row "
                                + currentLineNum + " is blank");
                    }
                    else if (isBoolean(currentColVal)) {
                        csvSchema.incrementBooleansCountAtColumn(colIndx);
                    } else if (isInteger(currentColVal)) {
                        csvSchema.incrementIntegersCountAtColumn(colIndx);
                    } else if (isFloat(currentColVal)) {
                        csvSchema.incrementFloatsCountAtColumn(colIndx);
                    } else {
                        CsvColumnPattern pattern = CsvColumnPattern.of(currentColVal);
                        csvSchema.colAt(colIndx).put(pattern);
                    }
                }
                if (!validationResult.isValid()
                        && validationResultStrategy.shouldStopAtFirstError()) {
                    return validationResult;
                }
            }

            // Do analysis on column data analysis(it total rows are more than one)
            if (currentLineNum > 1 && csvSchema != null) {
                csvSchema.setTotalRows(currentLineNum);
                new CsvSchemaValidator().validateAndAddError(
                        csvSchema, validationResult, currentLineNum);
            }
            return validationResult;
        } catch (ParsingException ex) {
            validationResult.addError(ex.getMessage());
            return validationResult;
        }
    }

    /**
     * Tests whether a string is float or not
     * @param s string value
     * @return boolean, whether given string an float or not
     */
    private boolean isFloat(String s) {
        /*try {
            Float.parseFloat(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }*/
        return NumberUtils.isCreatable(s);
    }

    /**
     * Tests whether a string is integer or not
     * @param s string value
     * @return boolean, whether given string an integer or not
     */
    private static boolean isInteger(String s) {
        final int radix = 10;
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                }
                else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isBoolean(String s) {
        if (s == null) {
            return false;
        }
        return BOOLEAN_TRUE.equalsIgnoreCase(s) || BOOLEAN_FALSE.equalsIgnoreCase(s);
    }

    /**
     * Guess whether file has a header line or not
     * @return boolean csv representing file has a header line or not
     * @throws ParsingException if fails
     */
    private boolean guessFileHasHeaderOrNot() throws ParsingException {
        InputStreamReader inputStreamToFile = createInputStreamFrom(file);
        CsvHeaderDetector csvHeaderDetector =
                new CsvHeaderDetector(inputStreamToFile, allowComments, delimiter);
        return csvHeaderDetector.hasHeader();
    }

    private InputStreamReader createInputStreamFrom(File file) throws ParsingException {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException ex) {
            throw new ParsingException("file not found!");
        }
    }
}
