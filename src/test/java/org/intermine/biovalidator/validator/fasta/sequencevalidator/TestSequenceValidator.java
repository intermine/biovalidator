package org.intermine.biovalidator.validator.fasta.sequencevalidator;

import org.junit.Assert;
import org.junit.Test;

public class TestSequenceValidator {

    @Test
    public void testDnaSequenceValidator() {
        SequenceValidator validator = new NucleicAcidSequenceValidator();
        Assert.assertTrue(validator.isValid("ACTACGAGTAC"));
        Assert.assertTrue(validator.isValid("ATWD-"));
        Assert.assertTrue(validator.isValid("actg"));

        Assert.assertFalse(validator.isValid("EWKDIwjgNKAWOE"));
        //Assert.assertFalse(validator.isValid("ACTGbCTGA"));
        Assert.assertTrue(validator.isValid(""));
    }
}
