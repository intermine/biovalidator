package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class BaseGff3ValidatorTest extends BaseValidatorTest {

    ValidationResult validateGff3FromStringAsInputSource(String gff3Content) throws ValidationFailureException {
        Validator validator = new Gff3Validator(createInMemoryInputStream(gff3Content));
        return validator.validate();
    }

    boolean isValidGff3Content(String gff3Content) throws ValidationFailureException {
        Validator validator = new Gff3Validator(createInMemoryInputStream(gff3Content));
        return validator.validate().isValid();
    }

    void assertInvalidGFF3ContentAndAssertErrMsg(String content, String expectedErrMsg) throws ValidationFailureException {
        ValidationResult result = validateGff3FromStringAsInputSource(content);
        assertFalse(result.isValid());
        assertEquals(expectedErrMsg, result.getErrorMessage());
    }
}
