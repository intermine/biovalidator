package org.intermine.biovalidator.validator.csv;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.intermine.biovalidator.validator.csv.CsvColumnValueType.*;

public class CsvPatternTest {

    @Test
    public void testPatternCreation() {
        CsvColumnPattern pattern = CsvColumnPattern.of("GRCh38/hg38 1p21.1-13.2(chr1:105468292-112190626)x1");
        System.out.println(pattern);
    }

    @Test
    public void testSimplePatternCreation() {
        CsvColumnPattern p1  = CsvColumnPattern.of("14781234");
        assertEquals(p1.getPatternList(), Collections.singletonList(DIGITS));

        CsvColumnPattern p2  = CsvColumnPattern.of("DNA");
        assertEquals(p2.getPatternList(), Collections.singletonList(LETTERS));

        CsvColumnPattern p3  = CsvColumnPattern.of("--%^^&");
        assertEquals(p3.getPatternList(), Collections.singletonList(SYMBOLS));
    }

    @Test
    public void testSimpleMixedPatternCreation() {
        CsvColumnPattern p1  = CsvColumnPattern.of("ab12376");
        assertEquals(p1.getPatternList(), listOf(LETTERS, DIGITS));

        CsvColumnPattern p2  = CsvColumnPattern.of("12367region");
        assertEquals(p2.getPatternList(), listOf(DIGITS, LETTERS));

        CsvColumnPattern p3  = CsvColumnPattern.of("123412::23948");
        assertEquals(p3.getPatternList(), listOf(DIGITS, SYMBOLS, DIGITS));

        CsvColumnPattern p4  = CsvColumnPattern.of("letters::digits");
        assertEquals(p4.getPatternList(), listOf(LETTERS, SYMBOLS, LETTERS));
    }

    @Test
    public void testComplexPattern() {
        assertEqualsWithoutCompression("Zen001gene::90", listOf(LETTERS, DIGITS, LETTERS, SYMBOLS,DIGITS));
        assertEqualsWithoutCompression("gene!!!!!!0000000000", listOf(LETTERS, SYMBOLS, DIGITS));
        assertEqualsWithoutCompression("SKU-10-1393-23", listOf(LETTERS, SYMBOLS, DIGITS, SYMBOLS, DIGITS,SYMBOLS, DIGITS));
        assertEqualsWithoutCompression("a1b2c3d4-e-5", listOf(LETTERS, DIGITS, LETTERS, DIGITS, LETTERS, DIGITS, LETTERS, DIGITS, SYMBOLS, LETTERS, SYMBOLS, DIGITS));
    }

    @Test
    public void testSkipSymbolsBetweenLetters() {
        assertEqualsWithoutCompression("csv, tsv", listOf(LETTERS));
        assertEqualsWithoutCompression("region_at23", listOf(LETTERS, DIGITS));
        assertEqualsWithoutCompression("united states (US)", listOf(LETTERS));
        assertEqualsWithoutCompression("csv & tsv", listOf(LETTERS));
        assertEqualsWithoutCompression("DNS'A1236172", listOf(LETTERS, DIGITS));
        assertEqualsWithoutCompression("DNS\"A1236172", listOf(LETTERS, DIGITS));
        assertEqualsWithoutCompression("DNS'1236172", listOf(LETTERS, SYMBOLS, DIGITS));
        assertEqualsWithoutCompression("DNS\"1236172", listOf(LETTERS, SYMBOLS, DIGITS));
        assertEqualsWithoutCompression("united states (US)234523", listOf(LETTERS, SYMBOLS, DIGITS));
    }

    @Test
    public void testPatternCompression() {
        assertEqualsPattern("1,2,3,4,5,6,8,10,230,", listOf(DIGITS, SYMBOLS));
    }

    private <T> List<T> listOf(T...types) {
        return Arrays.stream(types)
                .collect(Collectors.toList());
    }

    private void assertEqualsPattern(String data, List<CsvColumnValueType> expectedPattern) {
        CsvColumnPattern p1  = CsvColumnPattern.of(data);
        assertEquals(expectedPattern, p1.getPatternList());
    }

    private void assertEqualsWithoutCompression(String data, List<CsvColumnValueType> expectedPattern) {
        CsvColumnPattern p1  = CsvColumnPattern.withoutCompression(data);
        assertEquals(expectedPattern, p1.getPatternList());
    }
}
