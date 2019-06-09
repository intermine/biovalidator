package org.intermine.biovalidator.api;

import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ValidatorHelperTest {

    private String filename = "/home/deepak/Desktop/intro";

    @Test
    public void testValidatorHelper() throws ValidationFailureException, FileNotFoundException {
        ValidationResult result = ValidatorHelper.validate(filename, SequenceType.DNA, false);
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidatorHelperWithDirectValidatorType() throws ValidationFailureException, FileNotFoundException {
        Assert.assertNotNull(ValidatorHelper.validateFastaDna(filename));
    }
}
