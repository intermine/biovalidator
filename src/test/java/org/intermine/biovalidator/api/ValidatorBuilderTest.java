package org.intermine.biovalidator.api;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class ValidatorBuilderTest {

    private String filename = "src/test/resources/fasta/valid/dna_sample.fa";

    @Test
    public void testValidatorBuilderCreationWithFile() throws ValidationFailureException {
        ValidationResult result = ValidatorBuilder.withFile(filename, SequenceType.DNA)
                .build().validate();
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidatorBuilderCreationWithType() {
        //Validator result = ValidatorBuilder.ofType(new FastaValidator(filename, SequenceType.ALL)).enableWarnings().build();
        //Assert.assertNotNull(result);
    }

    @Test
    public void testValidatorBuilderWithCustomization() throws ValidationFailureException {
        ValidatorBuilder.withFile(filename, SequenceType.DNA)
                .enableWarnings()
                .disableErrors()
                .stopAtFirstError()
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
