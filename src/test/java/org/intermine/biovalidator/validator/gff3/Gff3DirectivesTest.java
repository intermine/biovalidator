package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Gff3DirectivesTest extends BaseGff3ValidatorTest {

    private static final String INVALID_VERSION_ERR_MSG = "Invalid Gff file! first line must be a header line "
            + "and version version must be in the format 3.#.#";

    private static final String INVALID_MISSING_ERR_MSG = "Invalid gff3 file! first line must be a header line";

    @Test
    public void testValidGff3Version() throws ValidationFailureException {
        String content = "##gff-version 3.2.1";
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testInvalidVerionLessThan3() throws ValidationFailureException {
        String content = "##gff-version 2";
        assertInvalidGFF3ContentAndAssertErrMsg(content, INVALID_VERSION_ERR_MSG);
    }

    @Test
    public void testInvalidVerionGreaterThan4() throws ValidationFailureException {
        String content = "##gff-version 45";
        assertInvalidGFF3ContentAndAssertErrMsg(content, INVALID_VERSION_ERR_MSG);
    }

    @Test
    public void testInvalidVersionNumber() throws ValidationFailureException {
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version       ", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version #", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version #.#.#", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 3.x.x", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 3.3.x", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 3.x.x", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 3.a.b", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 8.1.-", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 3.3.3.4", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 3.#", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 3.9.#", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 33", INVALID_VERSION_ERR_MSG);

    }

    @Test
    public void testValidGff3Content() throws ValidationFailureException {
        String content = "##gff-version 3\n";
        ValidationResult result = validateGff3FromStringAsInputSource(content);
        assertTrue(result.isValid());
    }

    @Test
    public void testInValidGff3Content() throws ValidationFailureException {
        assertInvalidGFF3ContentAndAssertErrMsg("", INVALID_VERSION_ERR_MSG); // test empty content
        assertInvalidGFF3ContentAndAssertErrMsg("     ", INVALID_MISSING_ERR_MSG); // test empty content
        assertInvalidGFF3ContentAndAssertErrMsg(">>1231231132 1312 134", INVALID_MISSING_ERR_MSG); // test empty content
        assertInvalidGFF3ContentAndAssertErrMsg("adkasjdkjas", INVALID_MISSING_ERR_MSG); // test empty content
        assertInvalidGFF3ContentAndAssertErrMsg("#Fasta", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("#GFF3", INVALID_VERSION_ERR_MSG);
        assertInvalidGFF3ContentAndAssertErrMsg("#gff-version 3", INVALID_VERSION_ERR_MSG); //invalid start with single #
        assertInvalidGFF3ContentAndAssertErrMsg("##gff-version 33", INVALID_VERSION_ERR_MSG);
    }
}
