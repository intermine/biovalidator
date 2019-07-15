package org.intermine.biovalidator.validator.fasta.validator;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.ValidatorType;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class FastaProteinValidatorTest extends BaseValidatorTest {

    @Test
    public void testDNASequence() throws ValidationFailureException {
        String filename = "valid/protein/protein.fa";
        Validator validator = ValidatorBuilder.withFile(path(filename), ValidatorType.FASTA_PROTEIN)
                .enableWarnings()
                .disableStrictValidation()
                .build();
        ValidationResult result = validator.validate();
        assertTrue(result.isValid());

        List<String> warnings = getWarningsListFromValidationResult(result);
        String warningMsg = "number of sequence length exceed 80 at line 12";

        assertEquals(warningMsg, warnings.get(0));
    }
}
