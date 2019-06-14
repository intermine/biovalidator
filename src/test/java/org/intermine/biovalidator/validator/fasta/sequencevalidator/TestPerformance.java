package org.intermine.biovalidator.validator.fasta.sequencevalidator;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Ignore;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

@Ignore
public class TestPerformance {

    private String filename = "/home/deepak/Documents/FASTA_FILES/protein.fa";
    private String smallProtein = "/home/deepak/Documents/FASTA_FILES/Protein/fasta_protein.fa";
    private String gffFile = "/home/deepak/Documents/FASTA_FILES/ref_GRCh38.p12_top_level.gff3";
    private String invalidDna = "/home/deepak/Documents/FASTA_FILES/dna/hs_ref_GRCh38.p12_chr2.fa";
    private String rna = "/home/deepak/Documents/FASTA_FILES/dna/rna.fa";

    @Test
    public void testPerformance() throws ValidationFailureException {
        long start = System.nanoTime();
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        ValidationResult result = ValidatorHelper.validateFastaDna(invalidDna);
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
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        System.out.println("Time Taken : " +
                TimeUnit.MILLISECONDS.convert((timeTaken), TimeUnit.NANOSECONDS));
        System.out.print("Memory Used : " + (afterUsedMem-beforeUsedMem));
    }
}
