package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.Message;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.api.WarningMessage;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class Gff3StartEndCordTest extends BaseGff3ValidatorTest{

    

    @Test
    public void testValidStatEndCoordinates() throws ValidationFailureException {
        String content = createGff3ContentWithStartAndEndCord(1, 100000);
        assertTrue(isValidGff3Content(content));
    }

    @Test
    public void testInvalidStartCord() throws ValidationFailureException {
        String zeroStart = createGff3ContentWithStartAndEndCord(0, 100000);
        assertFalse(isValidGff3Content(zeroStart));
    }

    @Test
    public void testStartGreaterThanEnd() throws ValidationFailureException {
        String startGreaterThanEndContent = createGff3ContentWithStartAndEndCord(10, 8);
        assertFalse(isValidGff3Content(startGreaterThanEndContent));
    }

    @Test
    public void testStartEndCordWhenSequenceRegionBoundIsDefined() throws ValidationFailureException {
        String file = getFullPath("gff3/valid/sequence-region-directive.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(file);
        System.out.println(result.getErrorMessage());
        assertTrue(result.isValid());
    }

    @Test
    public void testWrongStartEndCordWhenSequenceRegionBoundIsDefined() throws ValidationFailureException {
        String file = getFullPath("gff3/invalid/sequence-region-directive.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(file);
        assertFalse(result.isValid());
    }

    @Test
    public void testWrongStartEndCordBoundsButValidDueToIsCircularTrue() throws ValidationFailureException {
        String file = getFullPath("gff3/valid/sequence-region-out-of-bound.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(file);
        assertTrue(result.isValid());
    }

    // Test Un-parsable start and end coordinates //
    // test un-parsable start cord
    @Test
    public void testWrongUnParsableStartCoordinates() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithStartAndEndCordAsString("-", "234");
        String errMsg = "start coordinate value '-' is not a number at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    @Test
    public void testWrongUnParsableStringAsStartCoordinates() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithStartAndEndCordAsString("abhdas", "234");
        String errMsg = "start coordinate value 'abhdas' is not a number at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    @Test
    public void testUnknownAsStartCoordinates() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithStartAndEndCordAsString(".", "234");
        String errMsg = "start coordinate value '.' is not a number at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    @Test
    public void testWrongStartCoordinateFromFile() throws ValidationFailureException {
        String file = getFullPath("gff3/invalid/wrong-start-coordinate.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(file);
        assertFalse(result.isValid());
        String errMsg = "start coordinate value 'abc123' is not a number at line 12";
        assertEquals(errMsg, result.getErrorMessage());
    }

    // test un-parsable start cord
    @Test
    public void testWrongUnParsableEndCoordinates() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithStartAndEndCordAsString("1333", "-");
        String errMsg = "end coordinate value '-' is not a number at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    @Test
    public void testWrongUnParsableStringAsEndCoordinates() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithStartAndEndCordAsString("243", "intermine");
        String errMsg = "end coordinate value 'intermine' is not a number at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    @Test
    public void testUnknownAsEndCoordinates() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithStartAndEndCordAsString("6546", ".");
        String errMsg = "end coordinate value '.' is not a number at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    @Test
    public void testWrongEndCoordinateFromFile() throws ValidationFailureException {
        String file = getFullPath("gff3/invalid/wrong-end-coordinate.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(file);
        assertFalse(result.isValid());
        String errMsg = "end coordinate value 'biovalidator_wrong_end_cord' is not a number at line 13";
        assertEquals(errMsg, result.getErrorMessage());
    }

    @Test
    public void testWarningOnSequenceRegionDirectiveWithDuplicateSeqId() throws ValidationFailureException, FileNotFoundException {
        String file = getFullPath("gff3/invalid/sequence-region-with-duplicates-seqId.gff3");
        InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
        Validator validator = ValidatorBuilder.ofType(new Gff3Validator(isr))
            .enableErrors()
            .enableWarnings()
            .build();
        ValidationResult result = validator.validate();
        assertTrue(result.isValid());
        String msg = "Found more than one entry for ##sequence-region NC_000001.121 at line 10";
        Message warning = result.getWarningMessages().get(0);
        assertEquals(warning.getMessage(), msg);
    }

    private String createGff3ContentWithStartAndEndCord(long start, long end) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC.001\tRefSeq\tregion\t" + start +"\t" + end + "\t248956422\t.\t.\t.\tID=id0";
    }

    private String createGff3ContentWithStartAndEndCordAsString(String start, String end) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC.001\tRefSeq\tregion\t" + start +"\t" + end + "\t248956422\t.\t.\t.\tID=id0";
    }

}
