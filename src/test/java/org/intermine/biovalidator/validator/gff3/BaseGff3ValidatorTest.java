package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Base Test class for GFF3 Tests containing some helper metods
 */
public class BaseGff3ValidatorTest extends BaseValidatorTest {

    /**
     * Validate GFF3 from string instead of file by creating in-memory input stream
     * @param gff3Content string as GFF3 content
     * @return validation result
     * @throws ValidationFailureException if validation fails
     */
    ValidationResult validateGff3FromStringAsInputSource(String gff3Content) throws ValidationFailureException {
        Validator validator = new Gff3Validator(createInMemoryInputStream(gff3Content));
        return validator.validate();
    }

    /**
     * Validate GFF3 from string instead of file by creating in-memory input stream
     * @param gff3Content string as GFF3 content
     * @return boolean, whether validation succeed or failed
     * @throws ValidationFailureException if validation fails
     */
    boolean isValidGff3Content(String gff3Content) throws ValidationFailureException {
        Validator validator = new Gff3Validator(createInMemoryInputStream(gff3Content));
        ValidationResult result = validator.validate();
        return result.isValid();
    }

    /**
     * Validate GFF3 from string as content and asserts that validation fails with a expected message
     * @param content string as GFF3 content
     * @param expectedErrMsg expected error message to be asserted when validation fails
     * @throws ValidationFailureException if validation fails
     */
    void assertInvalidGFF3ContentAndAssertErrMsg(String content, String expectedErrMsg) throws ValidationFailureException {
        ValidationResult result = validateGff3FromStringAsInputSource(content);
        assertFalse(result.isValid());
        assertEquals(expectedErrMsg, result.getErrorMessage());
    }
}
