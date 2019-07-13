package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class Gff3DirectivesTest extends BaseGff3ValidatorTest {


    @Test
    public void testValidGff3Version() throws ValidationFailureException {
        String content = "##gff-version 3.2.1";
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testInvalidVerionLessThan3() throws ValidationFailureException {
        String content = "##gff-version 2";
        assertFalse(isValidGff3Content(content));
    }

    @Test
    public void testInvalidVerionGreaterThan4() throws ValidationFailureException {
        String content = "##gff-version 45";
        assertFalse(isValidGff3Content(content));
    }

    @Test
    public void testInvalidVersionNumber() throws ValidationFailureException {
        assertFalse(isValidGff3Content("##gff-version 3.x.x"));
        assertFalse(isValidGff3Content("##gff-version 3.3.x"));
        assertFalse(isValidGff3Content("##gff-version 3.x.x"));
        assertFalse(isValidGff3Content("##gff-version 3.a.b"));
        assertFalse(isValidGff3Content("##gff-version 8.1.-"));
        assertFalse(isValidGff3Content("##gff-version 3.3.3.4"));
        assertFalse(isValidGff3Content("##gff-version 3.#"));
        assertFalse(isValidGff3Content("##gff-version 3.9.#"));
        assertFalse(isValidGff3Content("##gff-version 33"));
    }

    @Test
    public void testValidGff3Content() throws ValidationFailureException {
        String content = "##gff-version 3\n";
        ValidationResult result = validateGff3FromStringAsInputSource(content);
        assertTrue(result.isValid());
    }

    @Test
    public void testInValidGff3Content() throws ValidationFailureException {
        assertFalse(isValidGff3Content("")); // test empty content
        assertFalse(isValidGff3Content("#Fasta"));
        assertFalse(isValidGff3Content("#GFF3"));
        assertFalse(isValidGff3Content("#gff-version 3")); //invalid start with single #
        assertFalse(isValidGff3Content("##gff-version 33"));
    }
}
