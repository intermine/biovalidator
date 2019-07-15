package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class Gff3ScoreStrandAndPhaseTest extends BaseGff3ValidatorTest{

    @Test
    public void testValidScoreWithEmptyValue() throws ValidationFailureException {
        String content = createGff3ContentWithScore(".");
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testValidScoreWithFloatingPoingNumber() throws ValidationFailureException {
        String content = createGff3ContentWithScore("1.5");
        assertTrue(isValidGff3Content(content));

        String bigFloatingPoingNumber = createGff3ContentWithScore("1189232313.2323");
        assertTrue(isValidGff3Content(bigFloatingPoingNumber));
    }

    @Test
    @Ignore
    public void testValidScoreWithEBasedFloatingPointNumbers() throws ValidationFailureException {
        String content = createGff3ContentWithScore("10e6");
        assertTrue(isValidGff3Content(content));
    }

    @Test
    @Ignore
    public void testValidScoreWithFBasedFloatingPointNumbers() throws ValidationFailureException {
        String content = createGff3ContentWithScore("10e4");
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testInvalidNegativeScoreValue() throws ValidationFailureException {
        String negNumberContent = createGff3ContentWithScore("-1.5");
        assertFalse(isValidGff3Content(negNumberContent));
    }

    @Test
    @Ignore //interger should not be allowed
    public void testInvalidScoreValue() throws ValidationFailureException {
        String content = createGff3ContentWithScore("10");
        assertFalse(isValidGff3Content(content));
    }

    @Test
    public void testInvalidScoreValueAsString() throws ValidationFailureException {
        String content = createGff3ContentWithScore("score");
        assertFalse(isValidGff3Content(content));
    }

    @Test
    public void testInvalidScoreValueWithSymbols() throws ValidationFailureException {
        String contentWithScoreAsPlus = createGff3ContentWithScore("+");
        assertFalse(isValidGff3Content(contentWithScoreAsPlus));

        String contentWithScoreAsMinus = createGff3ContentWithScore("+");
        assertFalse(isValidGff3Content(contentWithScoreAsMinus));

        String contentWithScoreAsQuestionMark = createGff3ContentWithScore("?");
        assertFalse(isValidGff3Content(contentWithScoreAsQuestionMark));
    }

    private String createGff3ContentWithScore(String score) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC101.2382\tRefSeq\tregion\t1\t248956422\t" + score + "\t+\t.\tID=id0";
    }

    private String createGff3ContentWithScoreStrandAndPhase(String score, String strand, String phase) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC01.101\tRefSeq\tregion\t1\t248956422\t" + score + "\t" + strand + "\t" + phase +"\tID=id0";
    }
}
