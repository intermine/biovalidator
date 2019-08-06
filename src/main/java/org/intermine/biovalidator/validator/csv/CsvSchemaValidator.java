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
    private static final double PATTERN_SIMILARITY_STRICT_RATE = 0.85; // between 0 and 1
    private static final double PATTERN_SIMILARITY_PERMISSIVE_RATE = 0.7; // between 0 and 1

    private boolean isStrictValidation;

    /**
     * Construct CsvSchemaValidator with whether to validate strictly or not
     * @param isStrictValidation whether to validate strictly or not
     */
    public CsvSchemaValidator(boolean isStrictValidation) {
        this.isStrictValidation = isStrictValidation;
    }

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

    /**
     * Validates a single csv-schema column
     */
    private void validateCsvColumn(CsvColumnMatrics column,
                                   ValidationResult validationResult,
                                   long totalRows,
                                   int columnIndex) {
        boolean isBooleanType = addWarningIfInconsistentSingleTypeData(
                validationResult, totalRows, column.getBooleansCount(), columnIndex, "boolean");

        boolean isIntegerType = addWarningIfInconsistentSingleTypeData(validationResult, totalRows,
                column.getIntegersCount(), columnIndex, "integer");

        boolean isFloatType = addWarningIfInconsistentSingleTypeData(validationResult, totalRows,
                column.getFloatsCount(), columnIndex, "float");

        // If data in the column does not contain single type values but rather has mixed data,
        // then check for inconsistency from the patterns created from column data
        if (allFalse(isBooleanType, isIntegerType, isFloatType)) {
            // if there are booleans or integers in the column and column is not recognized
            // as a single-type, then create a equivalent pattern representing the values
            // and add the created pattern into column's pattern list, in-order to be validated
            // for consistency check.
            if (column.getBooleansCount() > 0) {
                column.addPattern(CsvColumnPattern.valueOf("true")); //pattern representing booleans
            }
            if (column.getIntegersCount() > 0) {
                column.addPattern(CsvColumnPattern.digitattern());
            }
            normalizeColumnPatterns(column); // merge similar pattern into one.
            if (column.getColumnDataPatterns().size() > 1) {
                //validate only if there are more than one type of pattern in the column
                validateColumnPattern(column, validationResult, totalRows, columnIndex);
            }
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

    /**
     * Check whether a column is of single-type(e.g. integer, boolean, floats) or not.
     * It add warning if it found most of the values are single-types but few are not.
     * @return whether column is of single-typed or not
     */
    private boolean addWarningIfInconsistentSingleTypeData(ValidationResult validationResult,
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

    /**
     * Validates pattern consistency of a column
     */
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

    /**
     * For a given column, It checks for pattern that are approximately same, for each pattern
     * in a column, it check whether a similar pattern exist in the column or not, if similar
     * pattern is found, then it merge two similar pattern into one and also combine
     * the count of both pattern.
     * @param column instance of CsvColumnMatrics
     */
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

    /**
     * Finds best matching entry(i.e. Pattern) in a CsvColumnMatrics, matching is defined by text
     * similarity between the given pattern and the patterns in the column,
     * For similarity Matching, this method uses 'Jaro–Winkler distance'.
     *
     * <p>
     *     Note: As the length of each pattern is max up to 30, then using a edit-distance
     *     measure(Jaro–Winkler distance) should not not create much performance issue.
     * </p>
     *  <pre>
     *  @see
     *  <a href="https://en.wikipedia.org/wiki/Jaro%E2%80%93Winkler_distance">
     *     Jaro–Winkler distance</a>
     *  <a href="https://commons.apache.org/proper/commons-text/apidocs/org/apache/
     *  commons/text/similarity/JaroWinklerDistance.html">Apache commans implementation</a>
     *  </pre>
     * @param column CsvColumnMatrics consist of all patterns of a column
     * @param entryToMatch pattern that is to be matched
     * @return best matching pattern entry from given column
     */
    private Optional<Map.Entry<CsvColumnPattern, Integer>> findBestMatchingEntry(
            CsvColumnMatrics column, Map.Entry<CsvColumnPattern, Integer> entryToMatch) {
        org.apache.commons.text.similarity.SimilarityScore<Double> similarityScore =
                new JaroWinklerSimilarity();
        double maxSimilarityScore = Double.MIN_VALUE;
        Map<CsvColumnPattern, Integer> patterns = column.getColumnDataPatterns();
        Map.Entry<CsvColumnPattern, Integer> mostSimilarPattern = null;
        for (Map.Entry<CsvColumnPattern, Integer> entry: patterns.entrySet()) {
            if (!entry.equals(entryToMatch)) {
                double currentScore  = similarityScore.apply(
                        entry.getKey().getPattern(), entryToMatch.getKey().getPattern());
                if (currentScore > maxSimilarityScore) {
                    maxSimilarityScore = currentScore;
                    mostSimilarPattern = entry;
                }
            }
        }
        final double definedPatternSimilarityRate =
            isStrictValidation ? PATTERN_SIMILARITY_STRICT_RATE: PATTERN_SIMILARITY_PERMISSIVE_RATE;
        if (maxSimilarityScore > definedPatternSimilarityRate) {
            return Optional.ofNullable(mostSimilarPattern);
        } else {
            return Optional.empty();
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

        patternWarningMsg.append("\tvalues similar to pattern(")
                .append(maxPattern.getPattern()).append(") ").append(" '")
                .append(maxPattern.getData()).append("' has ")
                .append(columnDataPatterns.get(maxPattern)).append(" counts\n");

        patternWarningMsg.append("\tvalues similar to pattern(")
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
