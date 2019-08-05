package org.intermine.biovalidator.validator.csv;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.intermine.biovalidator.validator.csv.CsvColumnValueType.DIGITS;
import static org.intermine.biovalidator.validator.csv.CsvColumnValueType.LETTERS;
import static org.intermine.biovalidator.validator.csv.CsvColumnValueType.SYMBOLS;
import static org.junit.Assert.assertEquals;

public class CsvPatternTest {

    @Test
    public void testPatternCreation() {
        CsvColumnPattern pattern = CsvColumnPattern.valueOf("GRCh38/hg38 1p21.1-13.2(chr1:105468292-112190626)x1");
        CsvColumnPattern pattern2 = CsvColumnPattern.valueOf("BSP--0-?100");
        System.out.println(pattern2);
    }

    @Test
    public void testSimplePatternCreation() {
        CsvColumnPattern p1  = CsvColumnPattern.valueOf("14781234");
        assertEquals(p1.getPatternList(), Collections.singletonList(DIGITS));

        CsvColumnPattern p2  = CsvColumnPattern.valueOf("DNA");
        assertEquals(p2.getPatternList(), Collections.singletonList(LETTERS));

        CsvColumnPattern p3  = CsvColumnPattern.valueOf("--%^^&");
        assertEquals(p3.getPatternList(), Collections.singletonList(SYMBOLS));
    }

    @Test
    public void testSimpleMixedPatternCreation() {
        CsvColumnPattern p1  = CsvColumnPattern.valueOf("ab12376");
        assertEquals(p1.getPatternList(), listOf(LETTERS, DIGITS));

        CsvColumnPattern p2  = CsvColumnPattern.valueOf("12367region");
        assertEquals(p2.getPatternList(), listOf(DIGITS, LETTERS));

        CsvColumnPattern p3  = CsvColumnPattern.valueOf("123412::23948");
        assertEquals(p3.getPatternList(), listOf(DIGITS, SYMBOLS, DIGITS));

        CsvColumnPattern p4  = CsvColumnPattern.valueOf("letters::digits");
        assertEquals(p4.getPatternList(), listOf(LETTERS, SYMBOLS, LETTERS));
    }

    @Test
    public void testComplexPattern() {
        assertEqualsWithoutCompression("Zen001gene::90", listOf(LETTERS, DIGITS, LETTERS, SYMBOLS,DIGITS));
        assertEqualsWithoutCompression("gene!!!!!!0000000000", listOf(LETTERS, SYMBOLS, DIGITS));

        assertEqualsWithoutCompression("SKU-10-1393-23", listOf(
                LETTERS, SYMBOLS, DIGITS, SYMBOLS, DIGITS,SYMBOLS, DIGITS));

        assertEqualsWithoutCompression("a1b2c3d4-e-5", listOf(LETTERS, DIGITS, LETTERS, DIGITS, LETTERS,
                DIGITS, LETTERS, DIGITS, SYMBOLS, LETTERS, SYMBOLS, DIGITS));
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

    @Test
    public void testRandomPattern() {
        String randomData = "z7,l,z7p86-4v-g4-vdd--h-o77z2-x-e12-u8-yw-o9i-0-78-789-67.dfg" +
                "/5jqlh59l20-19-08-05 07:-9-788889/892-2:16 -UTC,er923o,as,sdf9k3.3/34q8908-w--";
        CsvColumnPattern pattern = CsvColumnPattern.valueOf(randomData);
        assertEquals(CsvColumnPattern.randomPattern(), pattern);
    }

    @Test
    public void testPatternDataExceedMaxLength() {
        String randomData = "z7,l,z7p86-4v-g4-vdd--h-o77z2-x-e12-u8-yw-o9i-0-78-789-67.dfg" +
                "/5jqlh59l20-19-08-05 07:-9-72";
        CsvColumnPattern pattern = CsvColumnPattern.valueOf(randomData);
        assertEquals(randomData.substring(0, CsvColumnPattern.MAX_PATTERN_DATA_LENGTH) + "...",
                pattern.getData());
    }

    @Test
    public void testPatternWithNoData() {
        CsvColumnPattern pattern = CsvColumnPattern.valueOf("");
        assertEquals(pattern, CsvColumnPattern.emptyPattern());
        assertEquals(0, pattern.getPattern().length());
        assertEquals(0, pattern.getData().length());
    }

    @Test
    public void testPatternDataAsNull() {
        CsvColumnPattern pattern = CsvColumnPattern.valueOf(null);
        assertEquals(0, pattern.getPattern().length());
        assertEquals(0, pattern.getData().length());
    }

    @SafeVarargs
    private static <T> List<T> listOf(final T...types) {
        return Arrays.stream(types)
                .collect(Collectors.toList());
    }

    private void assertEqualsPattern(String data, List<CsvColumnValueType> expectedPattern) {
        CsvColumnPattern p1  = CsvColumnPattern.valueOf(data);
        assertEquals(expectedPattern, p1.getPatternList());
    }

    private void assertEqualsWithoutCompression(String data, List<CsvColumnValueType> expectedPattern) {
        CsvColumnPattern p1  = CsvColumnPattern.withoutCompression(data);
        assertEquals(expectedPattern, p1.getPatternList());
    }
}
