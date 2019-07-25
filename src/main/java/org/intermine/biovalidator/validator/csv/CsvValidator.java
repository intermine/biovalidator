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

import org.intermine.biovalidator.api.ParsingException;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.parser.CsvParser;
import org.intermine.biovalidator.validator.AbstractValidator;

import javax.annotation.Nonnull;
import java.io.InputStreamReader;

/**
 * Validator for validating csv/tsv data
 * @author deepak kumar
 */
public class CsvValidator extends AbstractValidator
{
    private static final double SUCCESS_SCORE_BAR = 0.5;
    private CsvParser csvParser;
    private SimilarityScore similarityScore;

    /**
     * Construct CsvParser with a input source by auto-detecting delimiter
     * and with allowed '#' based comments
     *
     * @param inputStreamReader input source
     */
    public CsvValidator(InputStreamReader inputStreamReader) {
        this(inputStreamReader, true);
    }

    /**
     * Construct CsvParser with a input source and whether to allow comments or not
     * and by auto-detecting delimiter
     * @param inputStreamReader input source
     * @param allowComments whether to allow '#' based comments or not
     */
    public CsvValidator(InputStreamReader inputStreamReader, boolean allowComments) {
        this(inputStreamReader, allowComments, "");
    }

    /**
     * Construct CsvParser with a input source and whether to allow comments or not
     * and by auto-detecting delimiter
     * @param inputStreamReader input source
     * @param allowComments whether to allow '#' based comments or not
     * @param delimiter delimiter for column separator
     */
    public CsvValidator(InputStreamReader inputStreamReader, boolean allowComments,
                        String delimiter) {
        //this.inputStreamReader = inputStreamReader;
        this.csvParser = new CsvParser(inputStreamReader, allowComments, delimiter);
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
            if (!csvParser.hasNext()) {
                return validationResult;
            }
            // store first data
            String[] firstRow = csvParser.parseNext();
            int firstRowColumnCount = firstRow.length;
            long currentLineNum = 1;

            while (csvParser.hasNext()) {
                currentLineNum++;
                String[] currentRow = csvParser.parseNext();

                //check number of column is same or not
                if (currentRow.length != firstRowColumnCount) {
                    String warningMsg = "Wrong number of columns at line " + currentLineNum;
                    validationResult.addError(warningMsg);
                }

                if (!validationResult.isValid()
                        && validationResultStrategy.shouldStopAtFirstError()) {
                    return validationResult;
                }

                //check consistency of each column of current row with each column of the first row
                for (int i = 0; i < firstRowColumnCount; i++) {
                    double score = similarityScore.findSimilarityScore(firstRow[i], currentRow[i]);
                    if (score < SUCCESS_SCORE_BAR) {
                        String warningMsg = "Column " + (i + 1) + " at row " + currentLineNum
                                + " '" + currentRow[i] + "' is not matching '" + firstRow[i] + "'";
                        validationResult.addWarning(warningMsg);
                    }
                }
                /*if (!validationResult.isValid()
                        && validationResultStrategy.shouldStopAtFirstError()) {
                    return validationResult;
                }*/
            }
            return validationResult;
        } catch (ParsingException ex) {
            return validationResult;
        }
    }
}
