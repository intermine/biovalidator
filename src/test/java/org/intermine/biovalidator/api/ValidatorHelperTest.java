package org.intermine.biovalidator.api;

import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Assert;
import org.junit.Test;

public class ValidatorHelperTest {

    private String filename = "/";

    @Test
    public void testValidatorHelper() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validate(filename, SequenceType.DNA, false);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidatorHelperWithDirectValidatorType() throws ValidationFailureException {
        Assert.assertNotNull(ValidatorHelper.validateFastaDna(filename));
    }
}
