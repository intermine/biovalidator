package org.intermine.biovalidator.validator.fasta.validator;

import org.intermine.biovalidator.api.Message;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
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

        List<String> errors = getErrorsListFromValidationResult(result);
        assertEquals("File is empty", errors.get(0));
    }

    @Test
    public void testFileWithWhiteSpaceAndBlankLines() throws ValidationFailureException {
        String filename = "invalid/empty_with_space.fa";
        ValidationResult result = ValidatorHelper.validateFastaDna(path(filename));
        assertFalse(result.isValid());

        List<String> errors = getErrorsListFromValidationResult(result);
        assertEquals("File is not recognized as valid Fasta format", errors.get(0));
    }

    @Test
    public void testInvalidFastaFile() throws ValidationFailureException {
        ValidationResult result = ValidatorHelper.validateFastaDna(path("fasta_rules"));
        assertFalse(result.isValid());

        List<String> errors = getErrorsListFromValidationResult(result);
        assertEquals("File is not recognized as valid Fasta format", errors.get(0));
    }

    @Test
    public void testBinaryFileUnrecognizedWithFasta() throws ValidationFailureException {
        String file = "/home/deepak/Documents/Run.class"; //"valid/multi_seq.fa"
        ValidationResult result = ValidatorHelper.validateFasta(file);
        assertFalse(result.isValid());
        List<String> errors = getErrorsListFromValidationResult(result);
        assertEquals("File is not recognized as valid Fasta format", errors.get(0));
    }

    /* 2. Multiple Fasta Sequence Record in a single file */
    @Test
    public void testMultiSeqeunceFastaFile() throws ValidationFailureException {
        boolean isValid = ValidatorHelper.validateFasta(path("valid/multi_seq.fa")).isValid();
        assertTrue(isValid);
    }

    /* 3. Check file contains IUB/IUPAC codes */
    @Test
    public void testValidFastaDNASequence() throws ValidationFailureException {
        String filename = "valid/dna/dna_hs_ref_GRCh38.p12_chr9.fa";
        boolean isValid = ValidatorHelper.validateFasta(path(filename))
                .isValid();
        assertTrue(isValid);
    }

    @Test
    public void testInvalidCode() throws ValidationFailureException {
        String inputData = ">seq1\n" +
                           "7789870hdqwdqd21";
        InputStream is = new ByteArrayInputStream(inputData.getBytes());
        InputStreamReader reader = new InputStreamReader(is);
        Validator validator = new FastaValidator(reader, SequenceType.ALL);
        ValidationResult result = validator.validate();
        assertFalse(result.isValid());

        List<String> errors = getErrorsListFromValidationResult(result);
        assertEquals("Invalid letter 7 at line number 2, column 1", errors.get(0));
    }

    @Test
    public void testWarningOnExceedingDefinedSequenceLengthOnALine()
                                                throws ValidationFailureException {
        String data = ">seq1\n" +
          "ATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCA\n"+
          "ATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATGCATATGCG";
        ValidationResult result = new FastaValidator(
                createInMemoryInputStream(data), SequenceType.ALL).validate();

        assertTrue(result.isValid());

        List<String> warnings = getWarningsListFromValidationResult(result);
        assertEquals("number of sequence length exceed 80 at line 2", warnings.get(0));
        assertEquals("number of sequence length exceed 80 at line 3", warnings.get(1));
    }

    @Test
    public void testFileContainUniqueHeaderIdentifier () throws ValidationFailureException {
        String filename = "invalid/duplicate_headers.fa";
        ValidationResult result = ValidatorHelper.validateFasta(path(filename));
        assertFalse(result.isValid());

        List<String> errors = getErrorsListFromValidationResult(result);
        assertEquals("Duplicate sequence-id at line 5", errors.get(0));
    }

    @Test
    public void testHeaderIsNotEmpty() throws ValidationFailureException {
        String filename = "invalid/empty_header.fa";
        ValidationResult result = ValidatorHelper.validateFasta(path(filename));
        assertFalse(result.isValid());

        List<String> errors = getErrorsListFromValidationResult(result);
        assertEquals("Record '>' has empty sequence at line 2", errors.get(0));
    }

    private List<String> getErrorsListFromValidationResult(ValidationResult result) {
        return result.getErrorMessages()
                .stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());
    }

    private List<String> getWarningsListFromValidationResult(ValidationResult result) {
        return result.getWarningMessages()
                .stream()
                .map(Message::getMessage)
                .collect(Collectors.toList());
    }

    private InputStreamReader createInMemoryInputStream(String data) {
        InputStream is = new ByteArrayInputStream(data.getBytes());
        return new InputStreamReader(is);
    }

    @Test
    public void testValidateWholeFileWithStopAtFirstError() {

    }


    private String path(String filename) {
        return "src/test/resources/fasta/" + filename;
    }
}
