package org.intermine.biovalidator.validator.csv;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CsvColumnMatricsTest {

    @Test
    public void testBooleanCounts() {
        CsvColumnMatrics columnMatrics = new CsvColumnMatrics();
        int n = 11;
        for (int i = 0; i < n; i++) {
            columnMatrics.incrementBooleanCount();
        }
        assertEquals(n, columnMatrics.getBooleansCount());
    }

    @Test
    public void testIntegersCounts() {
        CsvColumnMatrics columnMatrics = new CsvColumnMatrics();
        int n = 22;
        for (int i = 0; i < n; i++) {
            columnMatrics.incrementIntegerCount();
        }
        assertEquals(n, columnMatrics.getIntegersCount());
    }

    @Test
    public void testFloatsCounts() {
        CsvColumnMatrics columnMatrics = new CsvColumnMatrics();
        int n = 333;
        for (int i = 0; i < n; i++) {
            columnMatrics.incrementFloatsCount();
        }
        assertEquals(n, columnMatrics.getFloatsCount());
    }

    @Test
    public void testPatternsCount() {
        CsvColumnMatrics columnMatrics = new CsvColumnMatrics();
        int n = 20;
        String patternValue = "";
        for (int i = 0; i < n; i++) {
            if ((i & 1) == 0) {
                patternValue += "Fizz";
            } else {
                patternValue += i;
            }
            columnMatrics.addPattern(CsvColumnPattern.withoutCompression(patternValue));
        }
        assertEquals(n, columnMatrics.getColumnDataPatterns().size());
        assertFalse(columnMatrics.isMaxPatternAllowedExceed());
    }

    @Test
    public void testPatternsExceedAllowdSize() {
        CsvColumnMatrics columnMatrics = new CsvColumnMatrics();
        int n = 320;
        String patternValue = "";
        for (int i = 0; i < n; i++) {
            if ((i & 1) == 0) {
                patternValue += "Fizz";
            } else {
                patternValue += i;
            }
            columnMatrics.addPattern(CsvColumnPattern.withoutCompression(patternValue));
        }
        assertEquals(CsvColumnMatrics.MAX_ALLOWED_PATTERNS_IN_A_COLUMN,
                columnMatrics.getColumnDataPatterns().size());
        assertTrue(columnMatrics.isMaxPatternAllowedExceed());
    }
}
