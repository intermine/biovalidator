package org.intermine.biovalidator.validator.fasta.sequencevalidator;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;

@Ignore
public class TestPerformance {

    private String protein = "/home/deepak/Documents/FASTA_FILES/protein.fa";
    private String smallProtein = "/home/deepak/Documents/FASTA_FILES/Protein/fasta_protein.fa";
    private String gffFile = "/home/deepak/Documents/FASTA_FILES/ref_GRCh38.p12_top_level.gff3";
    private String invalidDna = "/home/deepak/Documents/FASTA_FILES/dna/hs_ref_GRCh38.p12_chr2.fa";
    private String rna = "/home/deepak/Documents/FASTA_FILES/dna/rna.fa";
    private String bigRna = "/home/deepak/Documents/FASTA_FILES/dna/10GBrna.fa";

    @Test
    public void testPerformance() throws ValidationFailureException {
        long start = System.nanoTime();
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        ValidationResult result = ValidatorHelper.validateFastaProtein(bigRna);
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        if (result.isValid()) {
            System.out.println("Valid File");
            result.getWarningMessages().forEach(System.out::println);
        } else {
            System.out.println("Invalid file");
            result.getErrorMessages().forEach(System.out::println);
            result.getWarningMessages().forEach(System.out::println);
            System.out.println(result.getErrorMessages().size());
        }
        long timeTaken = System.nanoTime() - start;
        System.out.println("Time Taken : " +
                TimeUnit.MILLISECONDS.convert((timeTaken), TimeUnit.NANOSECONDS));
        System.out.print("Memory Used : " + (afterUsedMem-beforeUsedMem));
    }

    //@Test
    public void testFileNotRecognized() throws ValidationFailureException {
        String s = "A\nA\n>BA\nA";
        InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(s.getBytes()));
        ValidationResult result = new FastaValidator(isr, SequenceType.DNA).validate();
        assertFalse(result.isValid());
    }
}
