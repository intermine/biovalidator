package org.intermine.biovalidator.api;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorHelperTest {

    private String filename = "src/test/resources/fasta/valid/dna_sample.fa";

    @Test
    public void testValidatorHelper() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validateFasta(filename);
        if (!result.isValid()) {
            result.getErrorMessages().forEach(System.out::println);
        }
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidatorHelperWithDirectValidatorType() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validateFastaDna(filename);
        if (!result.isValid()) {
            result.getErrorMessages().forEach(System.out::println);
        }
        Assert.assertTrue(result.isValid());
    }
}
