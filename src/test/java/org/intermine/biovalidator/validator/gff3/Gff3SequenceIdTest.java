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
    public void testInValidGff3SeqId() throws ValidationFailureException {
        String content = createGff3ContentWithSeqId("NC_%"); // '%' is not allowed in seqId
        assertFalse(isValidGff3Content(content));
    }

    private String createGff3ContentWithSeqId(String seqId) {
        return "##gff-version 3" + System.lineSeparator()
                + seqId + "\tRefSeq\tregion\t1\t248956422\t.\t+\t.\tID=id0";
    }
}
