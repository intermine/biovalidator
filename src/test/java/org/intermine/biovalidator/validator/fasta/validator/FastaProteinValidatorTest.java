package org.intermine.biovalidator.validator.fasta.validator;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class FastaProteinValidatorTest extends BaseValidatorTest {

    @Test
    public void testDNASequence() throws ValidationFailureException {
        String filename = "valid/protein/protein.fa";
        ValidationResult result = ValidatorHelper.validateFastaProtein(path(filename));
        assertTrue(result.isValid());

        List<String> errors = getWarningsListFromValidationResult(result);
        String warningMsg = "number of sequence length exceed 80 at line 12";

        assertEquals(warningMsg, errors.get(0));
    }
}
