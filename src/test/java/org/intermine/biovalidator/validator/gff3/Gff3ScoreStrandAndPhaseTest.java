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
    public void testValidScientificNotationScoreValues() throws ValidationFailureException {
        assertTrue(isValidGff3Content(createGff3ContentWithScore("1.0E-4")));
        assertTrue(isValidGff3Content(createGff3ContentWithScore("1.0E-4")));
        assertTrue(isValidGff3Content(createGff3ContentWithScore("1.3e6")));

        assertTrue(isValidGff3Content(createGff3ContentWithScore("1.6E-35")));
        assertTrue(isValidGff3Content(createGff3ContentWithScore("1.3e+03")));
        assertTrue(isValidGff3Content(createGff3ContentWithScore("1.02e+03")));
        assertTrue(isValidGff3Content(createGff3ContentWithScore("0.999")));

        assertTrue(isValidGff3Content(createGff3ContentWithScore("1.58e+03")));
        assertTrue(isValidGff3Content(createGff3ContentWithScore("6.022e23")));
        assertTrue(isValidGff3Content(createGff3ContentWithScore("10e8")));
    }

    @Test
    public void testValidScoreWithEBasedFloatingPointNumbers() throws ValidationFailureException {
        String content = createGff3ContentWithScore("10e6");
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testValidScoreWithFBasedFloatingPointNumbers() throws ValidationFailureException {
        String content = createGff3ContentWithScore("10e4");
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testInvalidNegativeScoreValue() throws ValidationFailureException {
        String negNumberContent = createGff3ContentWithScore("-1.5");
        assertTrue(isValidGff3Content(negNumberContent));
    }

    @Test
    public void testSimpleIntegerAsScoreValue() throws ValidationFailureException {
        String content = createGff3ContentWithScore("10");
        assertTrue(isValidGff3Content(content));
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

    @Test
    public void testValidStrandValue() throws ValidationFailureException {
        assertTrue(isValidGff3Content(createGff3ContentWithStrand("+")));
        assertTrue(isValidGff3Content(createGff3ContentWithStrand("-")));
        assertTrue(isValidGff3Content(createGff3ContentWithStrand(".")));
        assertTrue(isValidGff3Content(createGff3ContentWithStrand("?")));
    }

    @Test
    public void testInValidStrandValue() throws ValidationFailureException {
        assertFalse(isValidGff3Content(createGff3ContentWithStrand(" ")));
        assertFalse(isValidGff3Content(createGff3ContentWithStrand("213")));
        assertFalse(isValidGff3Content(createGff3ContentWithStrand("strand")));
        assertFalse(isValidGff3Content(createGff3ContentWithStrand(" ")));
    }

    @Test
    public void testValidPhase() throws ValidationFailureException {
        assertTrue(isValidGff3Content(createGff3ContentWithPhase("0")));
        assertTrue(isValidGff3Content(createGff3ContentWithPhase("1")));
        assertTrue(isValidGff3Content(createGff3ContentWithPhase("2")));
        assertTrue(isValidGff3Content(createGff3ContentWithPhase(".")));
    }

    @Test
    public void testInvalidValidPhase1() throws ValidationFailureException {
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("3")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("10")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("a")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("!")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase(" ")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("-1")));
    }

    @Test
    public void testInvalidPhaseRequiredForCDSFeature() throws ValidationFailureException {
        String invalidGff3CDSFeatureContent = "##gff-version 3" + System.lineSeparator()
                + "NC01.101\tRefSeq\tCDS\t1\t248956422\t.\t.\t.\tID=id0";

        assertFalse(isValidGff3Content(invalidGff3CDSFeatureContent));
    }

    @Test
    public void testValidPhaseRequiredForCDSFeature() throws ValidationFailureException {
        String invalidGff3CDSFeatureContent = "##gff-version 3" + System.lineSeparator()
                + "NC01.101\tRefSeq\tCDS\t1\t248956422\t.\t.\t2\tID=id0";

        assertTrue(isValidGff3Content(invalidGff3CDSFeatureContent));
    }

    private String createGff3ContentWithScore(String score) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC101.2382\tRefSeq\tregion\t1\t248956422\t" + score + "\t+\t.\tID=id0";
    }

    private String createGff3ContentWithStrand(String strand) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC01.101\tRefSeq\tregion\t1\t248956422\t.\t" + strand + "\t.\tID=id0";
    }

    private String createGff3ContentWithPhase(String phase) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC01.101\tRefSeq\tregion\t1\t248956422\t.\t.\t"+ phase +"\tID=id0";
    }
}
