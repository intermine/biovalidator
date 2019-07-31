package org.intermine.biovalidator.validator.csv;

import com.univocity.parsers.csv.CsvFormat;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestCsvValidator extends BaseValidatorTest {

    @Test
    public void testCsvValidator() {
        try {
        //String simple = "/home/deepak/Documents/Intermine/FILES/CSV/5d331a9794f6200004000040.csv";
            String csv = "/home/deepak/Documents/Intermine/FILES/CSV/cars.csv";
            String sample = "/home/deepak/Documents/Intermine/FILES/CSV/Drosophila_2.na25.annot.csv";
            String variant_summary = "/home/deepak/Documents/Intermine/FILES/CSV/variant_summary_sample.csv";
            String filePath = getFullPath("csv/variant_summary_sample.csv");
            String anotation = "/home/deepak/Documents/Intermine/FILES/CSV/anotation/U133AGNF1B.gcrma.avg.csv";
            ValidationResult result = ValidatorHelper.validateCsv(filePath, true);

            System.out.println(result.getErrorMessage());
            if (result.getWarningMessages().size() > 0) {
                result.getWarningMessages().forEach(System.out::println);
            }
            assertTrue(result.isValid());
            //assertEquals(13, result.getWarningMessages().size());
        } catch (Exception e) {
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testPattern() {
        System.out.println(createCsvColumnPatternFromString("Chevrolet Chevelle Concours ()"));
    }

    private CsvColumnPattern createCsvColumnPatternFromString(String s) {
        List<CsvColumnValueType> columnValuePattern = new ArrayList<>();
        int len = s.length();
        CsvColumnValueType prevType = null;
        for (int i = 1; i < len; i++) {
            CsvColumnValueType currentType = CsvColumnValueType.getType(s.charAt(i));
            //skip while type is same
            while ((i + 1) < len && currentType == CsvColumnValueType.getType(s.charAt(i + 1))) {
                i++;
            }
            if ((i + 1) < len && prevType != null
                    && isGenerallyAcceptedStringSymbol(s.charAt(i))
                    && prevType == CsvColumnValueType.LETTERS
                    && CsvColumnValueType.getType(s.charAt(i + 1)) == CsvColumnValueType.LETTERS) {
                continue;
            } if ((i + 1) == len && (prevType == CsvColumnValueType.LETTERS)){
                continue;
            } else {
                if (currentType != prevType) {
                    columnValuePattern.add(currentType);
                }
            }
            prevType = currentType;
        }
        // if list of pattern in a single column value is greater than defined MAX pattern length,
        // then it is assumed that, the given column value has a random or very mixed pattern
        if (columnValuePattern.size() > CsvColumnPattern.MAX_PATTERN_LENGTH) {
            return CsvColumnPattern.randomPattern();
        }
        return CsvColumnPattern.of(columnValuePattern, s);
    }
    private boolean isGenerallyAcceptedStringSymbol(char c) {
        return c == ',' || c == '(' || c == ')' || c == '\'' || c == '\"' || c == '&';
    }
    @Test
    public void testAutodetection() throws Exception {
        CsvParserSettings settings = new CsvParserSettings();
        settings.detectFormatAutomatically();
        CsvParser parser = new CsvParser(settings);

        String s = "" +
                "1;2001-01-01;First row;1.1\n" +
                "2;2002-02-02;Second row;2.2\n" +
                "3;2003-03-03;Third row;3.3\n" +
                "4;2004-04-04;Fourth row;4.4";

        List<String[]> rows = parser.parseAll(new StringReader(s));

        CsvFormat format = parser.getDetectedFormat();
        assertEquals(format.getDelimiter(), ';');

    }
}
