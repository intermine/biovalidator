package org.intermine.biovalidator.validator.csv;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCsvValidator extends BaseValidatorTest {

    @Test
    public void testCsvValidator() throws ValidationFailureException {
        /*String simple = "/home/deepak/Documents/Intermine/FILES/CSV/5d331a9794f6200004000040.csv";
        String csv = "/home/deepak/Documents/Intermine/FILES/CSV/csv.csv";
        String sample = "/home/deepak/Documents/Intermine/FILES/CSV/variant_summary_sample.csv";*/
        String filePath = getFullPath("csv/variant_summary_sample.csv");
        ValidationResult result = ValidatorHelper.validateCsv(filePath, true);

        System.out.println(result.getErrorMessage());
        if (result.getWarningMessages().size() > 0) {
            result.getWarningMessages().forEach(System.out::println);
        }
        assertTrue(result.isValid());
        assertEquals(13, result.getWarningMessages().size());
    }
}
