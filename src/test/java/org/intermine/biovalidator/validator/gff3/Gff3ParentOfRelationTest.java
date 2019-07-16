package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Gff3ParentOfRelationTest extends BaseGff3ValidatorTest{

    @Test
    @Ignore
    public void testValidParentChildRelation() throws ValidationFailureException {
        //String filename = getFullPath("/gff3/valid/gencode.vM22.annotation.gff3");
        String filename = "/home/deepak/Documents/Intermine/FILES/gencode.vM22.annotation.gff3";
        ValidationResult result = ValidatorHelper.validateGff3(filename);
        result.getErrorMessages().forEach(System.out::println);
        assertTrue(result.isValid());
    }

    @Test
    public void testIDAttributeAsParentValue() throws ValidationFailureException {

    }

    @Test
    public void testNameAttributeAsParentValue() throws ValidationFailureException {

    }

    @Test
    public void testParentNotFound() throws ValidationFailureException {

    }
}
