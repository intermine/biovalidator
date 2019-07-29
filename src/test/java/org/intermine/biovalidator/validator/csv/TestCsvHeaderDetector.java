package org.intermine.biovalidator.validator.csv;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ParsingException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import static junit.framework.TestCase.assertTrue;

public class TestCsvHeaderDetector extends BaseValidatorTest {

    @Test
    public void testHeaderDetector() throws FileNotFoundException, ParsingException {
        String file = "/home/deepak/Documents/Intermine/FILES/CSV/csv.csv";
        InputStreamReader isr = new FileReader(file);
        CsvHeaderDetector detector = new CsvHeaderDetector(isr);
        assertTrue(detector.hasHeader());
    }
}
