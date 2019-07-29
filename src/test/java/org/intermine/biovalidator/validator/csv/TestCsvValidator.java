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
import java.util.List;

import static org.junit.Assert.*;

public class TestCsvValidator extends BaseValidatorTest {

    @Test
    public void testCsvValidator() {
        try {
        //String simple = "/home/deepak/Documents/Intermine/FILES/CSV/5d331a9794f6200004000040.csv";
            String csv = "/home/deepak/Documents/Intermine/FILES/CSV/cars.csv";
            String sample = "/home/deepak/Documents/Intermine/FILES/CSV/Drosophila_2.na25.annot.csv";
            String filePath = getFullPath("csv/variant_summary_sample.csv");
            ValidationResult result = ValidatorHelper.validateCsv(csv, true);

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
