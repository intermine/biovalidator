package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class Gff3SequenceIdTest extends BaseGff3ValidatorTest {

    private static final String INVALID_SEQ_ID_ERR_MSG = "Invalid Sequence Id '%s' at line %s";

    @Test
    public void testValidGff3SeqId() throws ValidationFailureException {
        String content = createGff3ContentWithSeqId("NC_000001.11");
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testValidEscapedSpaceInSeqId() throws ValidationFailureException {
        String seqIdWithSpace = createGff3ContentWithSeqId("chr1\\ 23");
        assertTrue(isValidGff3Content(seqIdWithSpace));

        String seqIdWithSpaceAndBracket = createGff3ContentWithSeqId("\\ 123\\>");
        assertTrue(isValidGff3Content(seqIdWithSpaceAndBracket));
    }

    @Test
    public void testInValidGff3SeqId() throws ValidationFailureException {
        String content = createGff3ContentWithSeqId("NC_%"); // '%' is not allowed in seqId
        String expectedErrorMsg = String.format(INVALID_SEQ_ID_ERR_MSG, "NC_%", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(content, expectedErrorMsg);

        String invalidStartContent = createGff3ContentWithSeqId(">NC"); // must not start with '>'
        String expectedInvalidStartErrorMsg = String.format(INVALID_SEQ_ID_ERR_MSG, ">NC", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(invalidStartContent, expectedInvalidStartErrorMsg);

        String invalidStartContentWithSpace = createGff3ContentWithSeqId(" NC"); // must not start with '>'
        String expectedInvalidStartErrorMsgWithSpace = String.format(INVALID_SEQ_ID_ERR_MSG, " NC", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(invalidStartContentWithSpace, expectedInvalidStartErrorMsgWithSpace);
    }


    private String createGff3ContentWithSeqId(String seqId) {
        return "##gff-version 3" + System.lineSeparator()
                + seqId + "\tRefSeq\tregion\t1\t248956422\t.\t+\t.\tID=id0";
    }
}
