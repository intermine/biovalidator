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

}
