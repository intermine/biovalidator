package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class GFF3SourceAndFeatureTypeTest extends BaseGff3ValidatorTest{

    @Test
    public void testValidSource() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithSource("DRefSeq");
        assertTrue(isValidGff3Content(gff3Content));
    }

    @Test
    public void testValidUnknownSource() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithSource(".");
        assertTrue(isValidGff3Content(gff3Content));
    }

    @Test
    public void testValidFeatureType() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithFeatureType("mRNA");
        assertTrue(isValidGff3Content(gff3Content));
    }

    @Test
    public void textWrongFeatureType() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithFeatureType("mRna");
        String errMsg = "unknown feature type 'mRna' at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    @Test
    public void textUnknownFeatureType() throws ValidationFailureException {
        String gff3Content = createGff3ContentWithFeatureType(".");
        String errMsg = "unknown feature type '.' at line 2";
        assertInvalidGFF3ContentAndAssertErrMsg(gff3Content, errMsg);
    }

    private String createGff3ContentWithSource(String source) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC.001\t" + source + "\tregion\t140\t180\t248956422\t.\t.\t.\tID=id0" + System.lineSeparator()
                + "NC.002\t" + source + "\tregion\t140\t149\t248956422\t.\t.\t.\tID=id0";
    }

    private String createGff3ContentWithFeatureType(String featureType) {
        return "##gff-version 3" + System.lineSeparator()
                + "NC.001\tRefSeq\t" + featureType + "\t140\t180\t248956422\t.\t.\t.\tID=id0" + System.lineSeparator()
                + "NC.002\t.\t" + featureType + "\t140\t149\t248956422\t.\t.\t.\tID=id0";
    }

}
