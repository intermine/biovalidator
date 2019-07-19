package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Gff3ParentOfRelationTest extends BaseGff3ValidatorTest{

    @Test
    public void testValidParentChildRelation() throws ValidationFailureException {
        String filename = getFullPath("/gff3/valid/valid-parent-of-relation.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(filename);
        assertTrue(result.isValid());
    }

    @Test
    public void testIDAttributeAsParentValue() throws ValidationFailureException {
        String filename = getFullPath("/gff3/valid/valid-parent-relation-with-value-stored-in-ID-tag.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(filename);
        assertTrue(result.isValid());
    }

    @Test
    public void testNameAttributeAsParentValue() throws ValidationFailureException {
        String filename = getFullPath("/gff3/invalid/valid-parent-relation-with-value-stored-in-Name-tag.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(filename);
        assertTrue(result.isValid());
    }

    @Test
    public void testParentNotFound() throws ValidationFailureException {
        String filename = getFullPath("/gff3/invalid/unknow-parent.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(filename);
        assertFalse(result.isValid());
        assertEquals(result.getErrorMessage(), "Parent 'rna1UNKNOW_PARENT' not found at line 18");
    }
}
