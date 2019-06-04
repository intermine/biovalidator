package org.intermine.biovalidator.api;

import org.junit.Assert;
import org.junit.Test;

public class ValidatorHelperTest {

    private String filename = "/";

    @Test
    public void testValidatorHelper() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validate(filename, ValidatorBuilder.Type.FASTA_DNA, false);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidatorHelperWithDirectValidatorType() throws ValidationFailureException {
        Assert.assertNotNull(ValidatorHelper.validateFastaDna(filename));
    }
}
