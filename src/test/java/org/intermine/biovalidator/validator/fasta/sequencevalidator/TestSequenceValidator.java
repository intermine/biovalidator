package org.intermine.biovalidator.validator.fasta.sequencevalidator;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestSequenceValidator {

    private SequenceValidator simpleNucleicValidator;
    private SequenceValidator proteinValidator;
    private SequenceValidator genericSequenceValidator;
    private SequenceValidator nucleicValidator;

    @Before
    public void setup() {
        simpleNucleicValidator = new NucleicAcidSequenceValidatorWithArray();
        proteinValidator = new ProteinSequenceValidator();
        genericSequenceValidator = new GenericSequenceValidator();
        nucleicValidator = new NucleicAcidSequenceValidator();
    }

    @Test
    public void testSimpleNucleicSequenceValidator() {
        assertTrue(simpleNucleicValidator.isValid("ACTACGAGTAC"));
        assertTrue(simpleNucleicValidator.isValid("ATWD-"));
        assertTrue(simpleNucleicValidator.isValid("actg"));

        assertFalse(simpleNucleicValidator.isValid("EWKDIwjgNKAWOE"));
        assertTrue(simpleNucleicValidator.isValid(""));

        assertTrue(simpleNucleicValidator.isValid("actgtactgatcga"));
        assertFalse(simpleNucleicValidator.isValid("actgtactgzzoootcga"));

        assertTrue(simpleNucleicValidator.isValid("A                   T"));
    }

    @Test
    public void testNucleicSequenceValidator() {
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
        assertTrue(simpleNucleicValidator.isValid("ACTACGAGTAC"));
        assertTrue(simpleNucleicValidator.isValid("ACTACGAGTAC      Uuattgc"));
        assertFalse(simpleNucleicValidator.isValid("763721673"));
        assertFalse(simpleNucleicValidator.isValid("ATGCZZZZZZ"));
    }

    @Test
    public void testInvalidNucleicCodes() {
        assertFalse(simpleNucleicValidator.isValid("E"));
        assertFalse(simpleNucleicValidator.isValid("F"));
        assertFalse(simpleNucleicValidator.isValid("I"));
        assertFalse(simpleNucleicValidator.isValid("J"));
        assertFalse(simpleNucleicValidator.isValid("L"));
        assertFalse(simpleNucleicValidator.isValid("O"));
        assertFalse(simpleNucleicValidator.isValid("p"));
        assertFalse(simpleNucleicValidator.isValid("Q"));
        assertFalse(simpleNucleicValidator.isValid("x"));
        assertFalse(simpleNucleicValidator.isValid("Z"));
        assertFalse(simpleNucleicValidator.isValid("**^"));
    }

    @Test
    public void testProteinSequences() {
        String proteinSeq =
                "MDDHFKRSRLSQEESSKSDLLCCPLPHTRDGAENVLCEPVTSGPVDVVVLAYSLDWTSLWEQQDQQEEQE";
        assertTrue(proteinValidator.isValid(proteinSeq));
        assertTrue(proteinValidator.isValid("skfkajd**-"));
        assertTrue(proteinValidator.isValid("           ZZZLKADIL"));
        assertFalse(proteinValidator.isValid("!@__"));
    }

    @Test
    public void testGenericSequenceValidator() {
        assertTrue(genericSequenceValidator.isValid("ACGTZAYIA"));
        assertTrue(genericSequenceValidator.isValid("        T"));
        assertTrue(genericSequenceValidator.isValid("*-."));
        assertFalse(genericSequenceValidator.isValid("!"));
        assertFalse(genericSequenceValidator.isValid(";"));
    }
}
