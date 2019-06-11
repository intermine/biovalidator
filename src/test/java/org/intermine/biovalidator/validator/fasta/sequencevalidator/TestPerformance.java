package org.intermine.biovalidator.validator.fasta.sequencevalidator;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class TestPerformance {

    private String filename = "/home/deepak/Documents/FASTA_FILES/protein.fa";
    private String smallProtein = "/home/deepak/Documents/FASTA_FILES/Protein/fasta_protein.fa";
    private String gffFile = "/home/deepak/Documents/FASTA_FILES/ref_GRCh38.p12_top_level.gff3";
    private String invalidDna = "/home/deepak/Documents/FASTA_FILES/dna/hs_ref_GRCh38.p12_chr2.fa";

    @Test
    public void testPerformance() throws FileNotFoundException, ValidationFailureException {
        long start = System.nanoTime();

        ValidationResult result = ValidatorHelper.validateFastaProtein(smallProtein);
        if (result.isValid()) {
            System.out.println("Valid File");
            result.getWarningMessages().forEach(System.out::println);
        } else {
            System.out.println("Invalid file");
            result.getErrorMessages().forEach(System.out::println);
            result.getWarningMessages().forEach(System.out::println);
        }
        long timeTaken = System.nanoTime() - start;
        System.out.println("Time Taken : " + TimeUnit.MILLISECONDS.convert((timeTaken), TimeUnit.NANOSECONDS));
    }

    //@Test
    public void testSequence() throws ValidationFailureException {
        InputStreamReader input = new InputStreamReader( new ByteArrayInputStream(data.getBytes()));
        new FastaValidator(input, SequenceType.DNA)
                .validate(result -> {
                    if (result.isValid()) {
                        System.out.print("Valid File");
                    } else {
                        System.out.print("Invalid File");
                        result.getErrorMessages().forEach(System.out::println);
                    }
                });
    }

    private String data = ">AB000263 |acc=AB000263|descr=Homo sapiens mRNA for prepro cortistatin like peptide, complete cds.|len=368\n" +
            "ACAAGATGCCATTGTCCCCCGGCCTCCTGCTGCTGCTGCTCTCCGGGGCCACGGCCACCGCTGCCCTGCC\n" +
            "CCTGGAGGGTGGCCCCACCGGCCGAGACAGCGAGCATATGCAGGAAGCGGCAGGAATAAGGAAAAGCAGC\n" +
            "CTCCTGACTTTCCTCGCTTGGTGGTTTGAGTGGACCTCCCAGGCCAGTGCCGGGCCCCTCATAGGAGAGG\n" +
            "AAGCTCGGGAGGTGGCCAGGCGGCAGGAAGGCGCACCCCCCCAGCAATCCGCGCGCCGGGACAGAATGCC\n" +
            "CTGCAGGAACTTCTTCTGGAAGACCTTCTCCTCCTGCAAATAAAACCTCACCCATGAATGCTCACGCAAG\n" +
            "TTTAATTACAGACCTGAA";

}
