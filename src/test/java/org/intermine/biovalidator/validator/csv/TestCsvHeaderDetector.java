package org.intermine.biovalidator.validator.csv;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ParsingException;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertFalse;


public class TestCsvHeaderDetector extends BaseValidatorTest {

    @Test
    public void testHeaderDetector() throws IOException {
        String sample = getFullPath("csv/variant_summary_sample.csv");

        CsvHeaderDetector detector = new CsvHeaderDetector(new FileReader(sample), true, "");
        assertFalse(detector.hasHeader());
    }

    @Test
    public void testAutodetection() throws IOException {
        String carsCsv = getFullPath("csv/variant_summary_sample_without_comments.csv");
        FileReader reader = new FileReader(carsCsv);
        CsvHeaderDetector headerDetector = new CsvHeaderDetector(reader, false, "");
        Assert.assertTrue(headerDetector.hasHeader());
    }
}
