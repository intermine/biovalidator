package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.ValidatorType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Gff3AttributeTest extends BaseGff3ValidatorTest{

    @Test
    public void testValidAttributeContent() throws ValidationFailureException {
        String filename = getFullPath("/gff3/valid/valid-parent-of-relation.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(filename);
        assertTrue(result.isValid());
    }

    @Test
    public void testErrorOnDuplicateAttributeKey() throws ValidationFailureException {
        String filename = getFullPath("/gff3/invalid/duplicated_attributes.gff3");
        ValidationResult result = ValidatorHelper.validateGff3(filename);
        assertFalse(result.isValid());
        String expectedErrMsg = "Tag 'ID' is duplicated at line 14";
        assertEquals(expectedErrMsg, result.getErrorMessage());
    }

    @Test
    public void testEmptyKeyWarnning() throws ValidationFailureException {
        String filename = getFullPath("/gff3/invalid/empty-attribute-key.gff3");
        ValidationResult result = ValidatorBuilder.withFile(filename, ValidatorType.GFF3)
                .enableWarnings()
                .build()
                .validate();

        assertTrue(result.isValid());
        assertTrue(result.getWarningMessages().size() > 0);

        String expectedWarningMsg = "attribute tag's key is missing or empty for value 'genomic DNA' at line 4";
        String actualWarningMsg = result.getWarningMessages().get(0).toString();
        assertEquals(expectedWarningMsg, actualWarningMsg);
    }

    @Test
    public void testEmptyValueWarning() throws ValidationFailureException {
        String filename = getFullPath("/gff3/invalid/empty-attribute-value.gff3");
        ValidationResult result = ValidatorBuilder.withFile(filename, ValidatorType.GFF3)
                .enableWarnings()
                .build()
                .validate();

        assertTrue(result.isValid());
        assertTrue(result.getWarningMessages().size() > 0);

        String expectedWarningMsg = "attribute value is missing or empty for key 'Dbxref' at line 5";
        String actualWarningMsg = result.getWarningMessages().get(0).toString();
        assertEquals(expectedWarningMsg, actualWarningMsg);
    }

}
