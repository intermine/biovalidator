package org.intermine.biovalidator.validator.csv;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ParsingException;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;

import static junit.framework.TestCase.assertTrue;

@Ignore
public class TestCsvHeaderDetector extends BaseValidatorTest {

    @Test
    public void testHeaderDetector() throws FileNotFoundException, ParsingException {
        String file = "/home/deepak/Documents/Intermine/FILES/CSV/emp.csv";
        String file2 = "/home/deepak/Documents/Intermine/FILES/CSV/record.csv";
        String sample = "/home/deepak/Documents/Intermine/FILES/CSV/anotation/U133AGNF1B.gcrma.avg.csv";

        CsvHeaderDetector detector = new CsvHeaderDetector(new FileReader(sample), true, "");
        System.out.println(detector.hasHeader());
        //assertTrue(detector.hasHeader());
    }
}
