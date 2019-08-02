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
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.intermine.biovalidator.api.DefaultValidationResult;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.strategy.ValidationResultStrategy;
import org.intermine.biovalidator.validator.RuleValidator;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Schema rule validator, to validate consistency of columns
 * @author deepak kumar
 */
public class CsvSchemaValidator implements RuleValidator<CsvSchema>
{
    @Override
    public boolean validateAndAddError(CsvSchema csvSchema,
                                       ValidationResult validationResult,
                                       long currentLineOfInput) {
        ValidationResultStrategy strategy =
                ((DefaultValidationResult) validationResult).getResultStrategy();
        // iterate over each column
        for (int i = 0; i < csvSchema.getColumnLength(); i++) {
            validateCsvColumn(csvSchema.colAt(i), validationResult, csvSchema.getTotalRows(), i);
        }
        return true;
    }

    private void validateCsvColumn(CsvColumnMatrics column,
                                   ValidationResult validationResult,
                                   long totalRows,
                                   int columnIndex) {
        final int minSingleTypePercent = 80;
        boolean isSingleType = false;
        if (column.getBooleansCount() > 0) {
            double percentageOfSingleType = calcPercentage(column.getBooleansCount(), totalRows);
            if (percentageOfSingleType >= minSingleTypePercent) {
                isSingleType = true;
            }
            if (percentageOfSingleType > minSingleTypePercent && percentageOfSingleType < 100) {
                String warningMsg  = String.format("data is not consistent in column "
                        + (columnIndex + 1) + ", %.2f of data is "
                        + "boolean but few are not.", percentageOfSingleType);
                validationResult.addWarning(warningMsg);
            }
        }
        if (column.getIntegersCount() > 0) {
            double percentageOfSingleType = calcPercentage(column.getIntegersCount(), totalRows);
            if (percentageOfSingleType >= minSingleTypePercent) {
                isSingleType = true;
            }
            if (percentageOfSingleType > minSingleTypePercent && percentageOfSingleType < 100) {
                String warningMsg  = String.format("data is not consistent in column "
                        + (columnIndex + 1) + ", %.2f of data is "
                        + "integer but few are not.", percentageOfSingleType);
                validationResult.addWarning(warningMsg);
            }
        }
        if (column.getFloatsCount() > 0) {
            double percentageOfSingleType = calcPercentage(column.getFloatsCount(), totalRows);
            if (percentageOfSingleType >= minSingleTypePercent) {
                isSingleType = true;
            }
            if (percentageOfSingleType > minSingleTypePercent && percentageOfSingleType < 100) {
                String warningMsg  = String.format("data is not consistent in column "
                        + (columnIndex + 1) + ", %.2f%% of data is "
                        + "float but few are not.", percentageOfSingleType);
                validationResult.addWarning(warningMsg);
            }
        }
        if (!isSingleType) {
            // data in the column does not contain single type but rather has mixed data
            normalizeColumnPatterns(column);
            validateColumnPattern(column, validationResult, totalRows, columnIndex);
        }
    }

    private void normalizeColumnPatterns(CsvColumnMatrics column) {
        Map<CsvColumnPattern, Integer> patterns = column.getColumnDataPatterns();
        Iterator<Map.Entry<CsvColumnPattern, Integer>> itr = patterns.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<CsvColumnPattern, Integer> entry = itr.next();
            findBestMatchingEntry(column, entry).ifPresent(e -> {
                e.setValue(entry.getValue() + e.getValue());
                itr.remove();
            });
        }
    }

    private Optional<Map.Entry<CsvColumnPattern, Integer>> findBestMatchingEntry(
            CsvColumnMatrics column, Map.Entry<CsvColumnPattern, Integer> entryToMatch) {
        org.apache.commons.text.similarity.SimilarityScore<Double> similarityScore =
                new JaroWinklerSimilarity();
        double maxScore = Double.MIN_VALUE;
        Map<CsvColumnPattern, Integer> patterns = column.getColumnDataPatterns();
        Map.Entry<CsvColumnPattern, Integer> mostSimilarPattern = null;
        for (Map.Entry<CsvColumnPattern, Integer> entry: patterns.entrySet()) {
            if (!entry.equals(entryToMatch)) {
                double currentScore  = similarityScore.apply(
                        entry.getKey().getPattern(), entryToMatch.getKey().getPattern());
                if (currentScore > maxScore) {
                    maxScore = currentScore;
                    mostSimilarPattern = entry;
                }
            }
        }
        if (maxScore > 0.85) {
            return Optional.of(mostSimilarPattern);
        } else {
            return Optional.empty();
        }
    }
    private void validateColumnPattern(CsvColumnMatrics column,
                                       ValidationResult validationResult,
                                       long totalRows,
                                       int columnIndex) {
        final double acceptablePercentageError = 30;
        Map<CsvColumnPattern, Integer> columnDataPatterns = column.getColumnDataPatterns();
        int size = columnDataPatterns.size();
        if (size < 1) {
            return;
        }
        long estimatedSizeForEachPattern = totalRows / columnDataPatterns.size();
        double totalPercentageError = 0.0D;
        for (Map.Entry<CsvColumnPattern, Integer> entry: columnDataPatterns.entrySet()) {
            int val = entry.getValue();
            long diff = Math.abs(estimatedSizeForEachPattern - val);
            totalPercentageError += calcPercentage(diff, estimatedSizeForEachPattern);
        }
        if ((totalPercentageError / size) > acceptablePercentageError) {
            validationResult.addWarning("data in column " + (columnIndex + 1)
                    + " does not confirms " + "to one or more pattern, look like "
                    + "this column has data with some random pattern");
        }
    }

    private double calcPercentage(long typeCount, long totalRows) {
        return ((double) typeCount / totalRows) * 100;
    }
}
