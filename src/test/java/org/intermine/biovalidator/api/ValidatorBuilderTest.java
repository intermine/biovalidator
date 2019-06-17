package org.intermine.biovalidator.api;
import org.intermine.biovalidator.validator.ValidatorType;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

public class ValidatorBuilderTest {

    private String filename = "src/test/resources/fasta/valid/dna_sample.fa";

    @Test
    public void testValidatorBuilderCreationWithFile() throws ValidationFailureException {
        ValidationResult result = ValidatorBuilder.withFile(filename, ValidatorType.FASTA_DNA)
                .build().validate();
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidatorBuilderCreationWithType() throws FileNotFoundException {
        InputStreamReader reader = new FileReader(filename);
        Validator result = ValidatorBuilder.ofType(new FastaValidator(reader, SequenceType.ALL)).enableWarnings().build();
        Assert.assertNotNull(result);
    }

    @Test
    public void testValidatorBuilderWithCustomization() throws ValidationFailureException {
        ValidatorBuilder.withFile(filename, ValidatorType.FASTA_DNA)
                .enableWarnings()
                .disableErrors()
                .enableStopAtFirstError()
                .enableStrictValidation()
                .build()
                .validate(result -> {
                    Assert.assertNotNull(result);
                    Assert.assertNotNull(result.getErrorMessages());
                    Assert.assertNotNull(result.getWarningMessages());
                    Assert.assertEquals(result.getErrorMessages().size(), result.totalError());
                    Assert.assertEquals(result.getWarningMessages().size(), result.totalWarnings());
                });

    }
}
