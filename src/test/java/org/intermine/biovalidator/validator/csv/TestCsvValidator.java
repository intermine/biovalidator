package org.intermine.biovalidator.validator.csv;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.ValidatorType;
import org.junit.Test;

import static org.intermine.biovalidator.validator.gff3.Gff3PerformanceTest.simpleBenchmark;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestCsvValidator extends BaseValidatorTest {

    @Test
    public void testCsvValidator() {
        simpleBenchmark(() -> {
            //String simple = "/home/deepak/Documents/Intermine/FILES/CSV/5d331a9794f6200004000040.csv";
            String csv = "/home/deepak/Documents/Intermine/FILES/CSV/cars.csv";
            String sample = "/home/deepak/Documents/Intermine/FILES/CSV/Drosophila_2.na25.annot.csv";
            String variant_summary = "/home/deepak/Documents/Intermine/FILES/CSV/variant_summary_sample.csv";
            String filePath = getFullPath("csv/variant_summary_sample.csv");
            String anotation = "/home/deepak/Documents/Intermine/FILES/CSV/U133AGNF1B.gcrma.avg.csv";
            ValidationResult result = ValidatorBuilder.withFile(filePath, ValidatorType.CSV)
                    .enableWarnings()
                    .withStrictValidation(true)
                    .build()
                    .validate();
            System.out.println(result.getErrorMessage());
            if (result.getWarningMessages().size() > 0) {
                result.getWarningMessages().forEach(System.out::println);
            }
            assertTrue(result.isValid());
        });
    }

    @Test
    public void testCsvValidatoHelper() {
        simpleBenchmark(() -> {
            String file = getFullPath("csv/variant_summary_sample.csv");
            ValidationResult result = ValidatorHelper.validateCsv(file, true);
            assertTrue(result.isValid());
        });
    }

    @Test
    public void testPattern() {
        String pattern = PatternUtils.createCsvColumnPatternFromString("Chevrolet Chevelle Concours ()");
        assertNotNull(pattern);
    }
}
