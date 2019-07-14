package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class Gff3SequenceIdTest extends BaseGff3ValidatorTest {

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
        assertFalse(isValidGff3Content(content));

        String invalidStartContent = createGff3ContentWithSeqId(">NC"); // must not start with '>'
        assertFalse(isValidGff3Content(invalidStartContent));

        String invalidStartContentWithSpace = createGff3ContentWithSeqId(" NC"); // must not start with '>'
        assertFalse(isValidGff3Content(invalidStartContentWithSpace));
    }


    private String createGff3ContentWithSeqId(String seqId) {
        return "##gff-version 3" + System.lineSeparator()
                + seqId + "\tRefSeq\tregion\t1\t248956422\t.\t+\t.\tID=id0";
    }
}
