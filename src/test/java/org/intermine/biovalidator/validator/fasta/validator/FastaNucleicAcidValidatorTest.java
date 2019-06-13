package org.intermine.biovalidator.validator.fasta.validator;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class FastaNucleicAcidValidatorTest {

    @Test
    public void testMultiSequenceValidFile() throws ValidationFailureException {
        String file = getFullPath("fasta/valid/dna_multiple.fa");
        ValidationResult result = ValidatorHelper.validateFastaDna(file);
        assertTrue(result.isValid());
    }

    @Test
    public void testMissingFirstHeader() throws ValidationFailureException {
        String file = getFullPath("fasta/invalid/dna_missing_first_header.fa");
        ValidationResult result = ValidatorHelper.validateFastaDna(file);
        assertFalse(result.isValid());
        dumpErrors(result);
    }

    @Test
    public void testMixedDnaData() throws ValidationFailureException {
        String file = getFullPath("fasta/valid/dna_mixed_data.fa");
        ValidationResult result = ValidatorHelper.validateFastaDna(file);
        assertTrue(result.isValid());
        dumpErrors(result);
    }


    private void dumpErrors(ValidationResult result) {
        result.getErrorMessages().forEach(System.out::println);
        result.getWarningMessages().forEach(System.out::println);
    }

    private String getFullPath(String filePath) {
        return "src/test/resources/" + filePath;
    }
}
