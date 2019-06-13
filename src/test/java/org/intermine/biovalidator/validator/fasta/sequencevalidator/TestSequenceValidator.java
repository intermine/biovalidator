package org.intermine.biovalidator.validator.fasta.sequencevalidator;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestSequenceValidator {

    private SequenceValidator nucleicValidator;
    private SequenceValidator proteinValidator;

    @Before
    public void setup() {
        nucleicValidator = new NucleicAcidSequenceValidatorWithArray();
        proteinValidator = new ProteinSequenceValidator();
    }
    @Test
    public void testDnaSequenceValidator() {
        assertTrue(nucleicValidator.isValid("ACTACGAGTAC"));
        assertTrue(nucleicValidator.isValid("ATWD-"));
        assertTrue(nucleicValidator.isValid("actg"));

        assertFalse(nucleicValidator.isValid("EWKDIwjgNKAWOE"));
        assertTrue(nucleicValidator.isValid(""));

        assertTrue(nucleicValidator.isValid("actgtactgatcga"));
        assertFalse(nucleicValidator.isValid("actgtactgzzoootcga"));

        assertTrue(nucleicValidator.isValid("A                   T"));
    }

    @Test
    public void testRnaSequences() {
        assertTrue(nucleicValidator.isValid("ACTACGAGTAC"));
        assertTrue(nucleicValidator.isValid("ACTACGAGTAC      Uuattgc"));
        assertFalse(nucleicValidator.isValid("763721673"));
        assertFalse(nucleicValidator.isValid("ATGCZZZZZZ"));
    }

    @Test
    public void testInvalidNucleicCodes() {
        assertFalse(nucleicValidator.isValid("E"));
        assertFalse(nucleicValidator.isValid("F"));
        assertFalse(nucleicValidator.isValid("I"));
        assertFalse(nucleicValidator.isValid("J"));
        assertFalse(nucleicValidator.isValid("L"));
        assertFalse(nucleicValidator.isValid("O"));
        assertFalse(nucleicValidator.isValid("p"));
        assertFalse(nucleicValidator.isValid("Q"));
        assertFalse(nucleicValidator.isValid("x"));
        assertFalse(nucleicValidator.isValid("Z"));
        assertFalse(nucleicValidator.isValid("**^"));
    }

    @Test
    public void testProteinSequences() {

    }

    @Test
    public void testGenericSequenceValidator() {

    }
}
