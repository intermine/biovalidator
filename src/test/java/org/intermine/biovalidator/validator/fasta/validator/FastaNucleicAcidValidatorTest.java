package org.intermine.biovalidator.validator.fasta.validator;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class FastaNucleicAcidValidatorTest extends BaseValidatorTest {

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
        //dumpErrors(result);
    }

    @Test
    public void testMixedDnaData() throws ValidationFailureException {
        String file = getFullPath("fasta/valid/dna_mixed_data.fa");
        ValidationResult result = ValidatorHelper.validateFastaDna(file);
        assertTrue(result.isValid());
        //dumpErrors(result);
    }

    @Test
    public void testDNASequence() throws ValidationFailureException {
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(data.getBytes()));
        new FastaValidator(isr, SequenceType.DNA)
                .validate(result -> assertTrue(result.isValid()));
    }

    private String data = ">AB000263 |acc=AB000263|descr=Homo sapiens mRNA for prepro " +
            "cortistatin like peptide, complete cds.|len=368\n" +
            "ACAAGATGCCATTGTCCCCCGGCCTCCTGCTGCTGCTGCTCTCCGGGGCCACGGCCACCGCTGCCCTGCC\n" +
            "CCTGGAGGGTGGCCCCACCGGCCGAGACAGCGAGCATATGCAGGAAGCGGCAGGAATAAGGAAAAGCAGC\n" +
            "CTCCTGACTTTCCTCGCTTGGTGGTTTGAGTGGACCTCCCAGGCCAGTGCCGGGCCCCTCATAGGAGAGG\n" +
            "AAGCTCGGGAGGTGGCCAGGCGGCAGGAAGGCGCACCCCCCCAGCAATCCGCGCGCCGGGACAGAATGCC\n" +
            "CTGCAGGAACTTCTTCTGGAAGACCTTCTCCTCCTGCAAATAAAACCTCACCCATGAATGCTCACGCAAG\n" +
            "TTTAATTACAGACCTGAA";
}
