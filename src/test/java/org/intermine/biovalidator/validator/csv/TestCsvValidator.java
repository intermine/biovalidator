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
            String filePath = getFullPath("csv/variant_summary_sample.csv");
            ValidationResult result = ValidatorBuilder.withFile(filePath, ValidatorType.CSV)
                    .enableWarnings()
                    //.withStrictValidation(false)
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
