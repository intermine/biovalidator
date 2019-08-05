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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.intermine.biovalidator.api.ValidationResult;
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
    private static final int MIN_SINGLE_TYPE_PERCENT = 80;

    @Override
    public boolean validateAndAddError(CsvSchema csvSchema,
                                       ValidationResult validationResult,
                                       long currentLineOfInput) {
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

        boolean isBooleanType = addWarningIfIncosistenceSingleTypeData(
                validationResult, totalRows, column.getBooleansCount(), columnIndex, "boolean");

        boolean isIntegerType = addWarningIfIncosistenceSingleTypeData(validationResult, totalRows,
                column.getIntegersCount(), columnIndex, "integer");

        boolean isFloatType = addWarningIfIncosistenceSingleTypeData(validationResult, totalRows,
                column.getFloatsCount(), columnIndex, "float");


        if (allFalse(isBooleanType, isIntegerType, isFloatType)) {
            // data in the column does not contain single type but rather has mixed data
            normalizeColumnPatterns(column);
            validateColumnPattern(column, validationResult, totalRows, columnIndex);
        }
    }

    private boolean allFalse(final boolean...types) {
        if (types == null) {
            throw new IllegalArgumentException("type array must not be null");
        }
        for (boolean element: types) {
            if (element) {
                return false;
            }
        }
        return true;
    }

    private boolean addWarningIfIncosistenceSingleTypeData(ValidationResult validationResult,
                                                        long totalRows,
                                                        long singleTypeCount,
                                                        int columnIndex,
                                                        String typeName) {
        if (singleTypeCount > 0) {
            double percentageOfSingleType = calcPercentage(singleTypeCount, totalRows);
            if (percentageOfSingleType > MIN_SINGLE_TYPE_PERCENT && percentageOfSingleType < 100) {
                long unmatchedRows = totalRows - singleTypeCount;
                String warningMsg  = "data is not consistent in column " + (columnIndex + 1)
                        + ", " + singleTypeCount +  " rows have " + typeName + " but "
                        + unmatchedRows + " rows has non-" + typeName + " values(s)";
                validationResult.addWarning(warningMsg);
                return true;
            }
        }
        return false;
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
        if (maxScore > 8.0) {
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
            validationResult.addWarning(createWarningMsg(columnDataPatterns, columnIndex));
        }
    }

    private String createWarningMsg(Map<CsvColumnPattern, Integer> columnDataPatterns,
                                    int columnIndex) {
        StringBuilder patternWarningMsg = new StringBuilder();
        patternWarningMsg.append("data in column ")
                .append(columnIndex + 1)
                .append(" does not confirms " + "to one or more pattern, look like "
                        + "this column has data with some random pattern:\n");

        ImmutablePair<CsvColumnPattern, CsvColumnPattern> maxAndMinPatterns =
                findMaxAndMinOccurrencePattern(columnDataPatterns);
        CsvColumnPattern maxPattern = maxAndMinPatterns.getLeft();
        CsvColumnPattern minPattern = maxAndMinPatterns.getRight();

        patternWarningMsg.append("\tvalues similar to this pattern(")
                .append(maxPattern.getPattern()).append(") ").append(" '")
                .append(maxPattern.getData()).append("' has ")
                .append(columnDataPatterns.get(maxPattern)).append(" counts\n");

        patternWarningMsg.append("\tvalues similar to this pattern(")
                .append(minPattern.getPattern()).append(") ").append(" '")
                .append(minPattern.getData()).append("' has ")
                .append(columnDataPatterns.get(minPattern)).append(" counts\n");
        return patternWarningMsg.toString();
    }

    /**
     * Find maximum and minimum key pairs in a Map
     * @param columnDataPatterns key-value pair map
     * @return pair of max and min keys
     */
    private ImmutablePair<CsvColumnPattern, CsvColumnPattern> findMaxAndMinOccurrencePattern(
            Map<CsvColumnPattern, Integer> columnDataPatterns) {
        CsvColumnPattern patternWithMaxOccurrence = null;
        CsvColumnPattern patternWithMinOccurrence = null;
        long maxOccurrence = Long.MIN_VALUE;
        long minOccurrence = Long.MAX_VALUE;
        for (Map.Entry<CsvColumnPattern, Integer> entry: columnDataPatterns.entrySet()) {
            if (entry.getValue() > maxOccurrence) {
                maxOccurrence = entry.getValue();
                patternWithMaxOccurrence = entry.getKey();
            }
            if (entry.getValue() < minOccurrence) {
                minOccurrence = entry.getValue();
                patternWithMinOccurrence = entry.getKey();
            }
        }
        return ImmutablePair.of(patternWithMaxOccurrence, patternWithMinOccurrence);
    }

    private double calcPercentage(long typeCount, long totalRows) {
        return ((double) typeCount / totalRows) * 100;
    }
}
