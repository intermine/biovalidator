package org.intermine.biovalidator.validator.fasta.validator;

import org.intermine.biovalidator.api.Message;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class FastaValidatorRuleTest {

    /* 1. Test Invalid File Format */
    @Test
    public void testFileNotEmpty() throws FileNotFoundException, ValidationFailureException {
        String filename = path("invalid/empty_file.fa");
        FileReader reader = new FileReader(filename);
        Validator validator = new FastaValidator(reader, SequenceType.DNA);
        ValidationResult result = validator.validate();
        assertFalse(result.isValid());

        List<String> errors = result.getErrorMessages()
                .stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());
        assertTrue(errors.contains("File is empty"));
    }

    @Test
    public void testFileWithWhiteSpaceAndBlankLines() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validateFastaDna(path("invalid/empty_with_space.fa"));
        assertFalse(result.isValid());

        List<String> errors = result.getErrorMessages()
                .stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());
        assertTrue(errors.contains("File is not recognized as valid Fasta format"));
    }

    @Test
    public void testInvalidFastaFile() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validateFastaDna(path("fasta_rules"));
        assertFalse(result.isValid());

        List<String> errors = result.getErrorMessages()
                .stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());
        assertTrue(errors.contains("File is not recognized as valid Fasta format"));
    }

    /* 2. Multiple Fasta Sequence Record in a single file */
    @Test
    public void testMultiSeqeunceFastaFile() {

    }

    /* 3. Check file contains IUB/IUPAC codes */
    @Test
    public void testValidFastaSequence() {

    }

    @Test
    public void testInvalidCode() {

    }

    @Test
    public void testValidFastaFile() {

    }

    @Test
    public void testWarningOnExceedingDefinedSequenceLengthOnALine() {

    }

    @Test
    public void testFileContainUniqueHeaderIdentifier () {

    }

    @Test
    public void testHeaderIsNotEmpty() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validateFasta(path("invalid/empty_header.fa"));
        assertFalse(result.isValid());

        List<String> errors = result.getErrorMessages()
                .stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());
        assertTrue(errors.contains("Record '>' has empty sequence at line 2"));
    }


    private String path(String filename) {
        return "src/test/resources/fasta/" + filename;
    }
}
