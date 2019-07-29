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
import org.intermine.biovalidator.api.ParsingException;
import org.intermine.biovalidator.api.ValidationFailureException;
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
    private static final double SUCCESS_SCORE_BAR = 0.5;
    private File file;
    private boolean allowComments;
    private boolean shouldAutoDetectDelimiter;
    private String delimiter;
    private SimilarityScore similarityScore;

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
        this.similarityScore = new SimilarityScore();
    }

    /**
     * <p>Validates a csv/tsv data</p>
     * <p>
     *     In-order to check consistency of a particular column, validator matches columns of first
     *     row to all other row in the data/file(current implementation for now).
     *     For each row and for each column, it find approximate similarity between columns of
     *     first and nth row, and based on the score it decide whether data is consistent or not.
     * </p>
     * @return validation result
     * @throws ValidationFailureException if validation or parsing fails
     */
    @Nonnull
    @Override
    public ValidationResult validate() throws ValidationFailureException {
        try {
            boolean doesFileHasHeader = guessFileHasHeaderOrNot();

            InputStreamReader inputStreamToFile = createInputStreamFrom(file);
            CsvParser csvParser = new CsvParser(
                    inputStreamToFile, doesFileHasHeader, allowComments, delimiter);

            final CsvColumnMatrics[] columnMatrics;

            //parsing the input file
            if (!csvParser.hasNext()) {
                return validationResult;
            }

            // store first data
            String[] firstRow = csvParser.parseNext();
            int totalColumns = firstRow.length;
            long currentLineNum = 1;

            columnMatrics = new CsvColumnMatrics[totalColumns];

            while (csvParser.hasNext()) {
                currentLineNum++;
                String[] currentRow = csvParser.parseNext();

                //check number of column is same or not
                if (currentRow.length != totalColumns) {
                    String warningMsg = "Wrong number of columns at line " + currentLineNum;
                    validationResult.addError(warningMsg);
                }

                if (!validationResult.isValid()
                        && validationResultStrategy.shouldStopAtFirstError()) {
                    return validationResult;
                }

                /*check consistency of each column of current row with each column of the first row
                for (int i = 0; i < totalColumns; i++) {
                    double score = similarityScore.findSimilarityScore(firstRow[i], currentRow[i]);
                    if (score < SUCCESS_SCORE_BAR) {
                        String warningMsg = "Column " + (i + 1) + " at row " + currentLineNum
                                + " '" + currentRow[i] + "' is not matching '" + firstRow[i] + "'";
                        validationResult.addWarning(warningMsg);
                    }
                }*/

                for (int colIndx = 0; colIndx < totalColumns; colIndx++) {
                    if (isBoolean(currentRow[colIndx])) {
                        columnMatrics[colIndx].incrementBooleanCount();
                    } else if (isInteger(currentRow[colIndx])) {
                        columnMatrics[colIndx].incrementIntegerCount();
                    } else if (isFloat(currentRow[colIndx])) {
                        columnMatrics[colIndx].incrementFloatsCount();
                    } else {
                        CsvColumnPattern pattern =
                                createCsvColumnPatternFromString(currentRow[colIndx]);
                        columnMatrics[colIndx].addColumnPattern(pattern);
                    }
                }
                if (!validationResult.isValid()
                        && validationResultStrategy.shouldStopAtFirstError()) {
                    return validationResult;
                }
            }

            // Do analysis on column matrics
            return validationResult;
        } catch (ParsingException ex) {
            validationResult.addError(ex.getMessage());
            ex.printStackTrace();
            return validationResult;
        }
    }

    private CsvColumnPattern createCsvColumnPatternFromString(String s) {
        return null; //TODO
    }

    private boolean isFloat(String s) {
        return true;
    }

    private boolean isInteger(String s) {
        return true;
    }

    private boolean isBoolean(String s) {
        return true;
    }

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
