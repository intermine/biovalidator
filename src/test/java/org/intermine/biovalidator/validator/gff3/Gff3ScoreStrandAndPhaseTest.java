package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Locale;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class Gff3ScoreStrandAndPhaseTest extends BaseGff3ValidatorTest{

    private static final String INVALID_SCORE_ERR_MSG = "score value must be a floating point number or '.'"
            + ", but found '%s' at line %d";

    private static final String INVALID_STRAND_ERR_MSG = "strand value must be one of ('-', '+', '?') "
            + "but found '%s' at line %d";

    private static final String INVALID_PHASE_ERR_MSG = "phase value can only be one of 0, 1, 2 or '.', but found "
            + "'%s' at line %d";

    private static final String REQUIRED_PHASE_ERR_MSG = "phase is required for CDS and can only be 0, 1 or 2 at line %d";

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
    public void testValidNegativeScoreValue() throws ValidationFailureException {
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
        String expectedErrMsg = String.format(INVALID_SCORE_ERR_MSG, "score", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(content, expectedErrMsg);
    }

    @Test
    public void testInvalidScoreValueWithSymbols() throws ValidationFailureException {
        String contentWithScoreAsPlus = createGff3ContentWithScore("+");
        String expectedErrMsgScoreAsPlus = String.format(INVALID_SCORE_ERR_MSG, "+", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(contentWithScoreAsPlus, expectedErrMsgScoreAsPlus);

        String contentWithScoreAsMinus = createGff3ContentWithScore("+");
        String expectedErrMsgScoreAsMinus = String.format(INVALID_SCORE_ERR_MSG, "+", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(contentWithScoreAsMinus, expectedErrMsgScoreAsMinus);

        String contentWithScoreAsQuestionMark = createGff3ContentWithScore("?");
        String expectedErrMsgScoreAsQuestionMark = String.format(INVALID_SCORE_ERR_MSG, "?", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(contentWithScoreAsQuestionMark, expectedErrMsgScoreAsQuestionMark);
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
        String emptyStrand = createGff3ContentWithStrand(" ");
        String expectedErrMsgScoreAsQuestionMark = String.format(INVALID_STRAND_ERR_MSG, " ", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(emptyStrand, expectedErrMsgScoreAsQuestionMark);

        assertFalse(isValidGff3Content(createGff3ContentWithStrand("213")));
        assertFalse(isValidGff3Content(createGff3ContentWithStrand("strand")));
        assertFalse(isValidGff3Content(createGff3ContentWithStrand("")));
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
        String invalidPhaseAsNumberContent = createGff3ContentWithPhase("3");
        String expectedErrMsg = String.format(INVALID_PHASE_ERR_MSG, "3", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(invalidPhaseAsNumberContent, expectedErrMsg);

        assertFalse(isValidGff3Content(createGff3ContentWithPhase("10")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("a")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("!")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase(" ")));
        assertFalse(isValidGff3Content(createGff3ContentWithPhase("-1")));

        String invalidPhaseAsSymbolContent = createGff3ContentWithPhase("@");
        String expectedErrMsgForSymbol = String.format(INVALID_PHASE_ERR_MSG, "@", 2);
        assertInvalidGFF3ContentAndAssertErrMsg(invalidPhaseAsSymbolContent, expectedErrMsgForSymbol);
    }

    @Test
    public void testInvalidPhaseRequiredForCDSFeature() throws ValidationFailureException {
        String invalidGff3CDSFeatureContent = "##gff-version 3" + System.lineSeparator()
                + "NC01.101\tRefSeq\tCDS\t1\t248956422\t.\t.\t.\tID=id0";

        String expectedErrMsg = String.format(REQUIRED_PHASE_ERR_MSG, 2);
        assertInvalidGFF3ContentAndAssertErrMsg(invalidGff3CDSFeatureContent, expectedErrMsg);
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
