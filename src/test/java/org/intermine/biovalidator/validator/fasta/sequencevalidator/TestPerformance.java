package org.intermine.biovalidator.validator.fasta.sequencevalidator;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.validator.fasta.FastaValidator;
import org.intermine.biovalidator.validator.fasta.SequenceType;
import org.junit.Test;

import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class TestPerformance {

    private String filename = "/home/deepak/Documents/FASTA_FILES/protein.fa";
    private String gffFile = "/home/deepak/Documents/FASTA_FILES/ref_GRCh38.p12_top_level.gff3";
    private String invalidDna = "/home/deepak/Documents/FASTA_FILES/dna/hs_ref_GRCh38.p12_chr2.fa";

    @Test
    public void testPerformance() throws FileNotFoundException, ValidationFailureException {
        long start = System.nanoTime();

        ValidationResult result = ValidatorHelper.validateFastaProtein(filename);
        if (result.isValid()) {
            System.out.println("Valid File");
        } else {
            System.out.println("Invalid file");
            result.getErrorMessages().forEach(System.out::println);
        }
        long timeTaken = System.nanoTime() - start;
        System.out.println("Time Taken : " + TimeUnit.MILLISECONDS.convert((timeTaken), TimeUnit.NANOSECONDS));
    }

    @Test
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

    private String data = ">HSBGPG Human gene for bone gla protein (BGP)\n" +
            "GGCAGATTCCCCCTAGACCCGCCCGCACCATGGTCAGGCATGCCCCTCCTCATCGCTGGGCACAGCCCAGAGGGT\n" +
            "ATAAACAGTGCTGGAGGCTGGCGGGGCAGGCCAGCTGAGTCCTGAGCAGCAGCCCAGCGCAGCCACCGAGACACC\n" +
            "ATGAGAGCCCTCACACTCCTCGCCCTATTGGCCCTGGCCGCACTTTGCATCGCTGGCCAGGCAGGTGAGTGCCCC\n" +
            "CACCTCCCCTCAGGCCGCATTGCAGTGGGGGCTGAGAGGAGGAAGCACCATGGCCCACCTCTTCTCACCCCTTTG\n" +
            "GCTGGCAGTCCCTTTGCAGTCTAACCACCTTGTTGCAGGCTCAATCCATTTGCCCCAGCTCTGCCCTTGCAGAGG\n" +
            "GAGAGGAGGGAAGAGCAAGCTGCCCGAGACGCAGGGGAAGGAGGATGAGGGCCCTGGGGATGAGCTGGGGTGAAC\n" +
            "CAGGCTCCCTTTCCTTTGCAGGTGCGAAGCCCAGCGGTGCAGAGTCCAGCAAAGGTGCAGGTATGAGGATGGACC\n" +
            "TGATGGGTTCCTGGACCCTCCCCTCTCACCCTGGTCCCTCAGTCTCATTCCCCCACTCCTGCCACCTCCTGTCTG\n" +
            "GCCATCAGGAAGGCCAGCCTGCTCCCCACCTGATCCTCCCAAACCCAGAGCCACCTGATGCCTGCCCCTCTGCTC\n" +
            "CACAGCCTTTGTGTCCAAGCAGGAGGGCAGCGAGGTAGTGAAGAGACCCAGGCGCTACCTGTATCAATGGCTGGG\n" +
            "GTGAGAGAAAAGGCAGAGCTGGGCCAAGGCCCTGCCTCTCCGGGATGGTCTGTGGGGGAGCTGCAGCAGGGAGTG\n" +
            "GCCTCTCTGGGTTGTGGTGGGGGTACAGGCAGCCTGCCCTGGTGGGCACCCTGGAGCCCCATGTGTAGGGAGAGG\n" +
            "AGGGATGGGCATTTTGCACGGGGGCTGATGCCACCACGTCGGGTGTCTCAGAGCCCCAGTCCCCTACCCGGATCC\n" +
            "CCTGGAGCCCAGGAGGGAGGTGTGTGAGCTCAATCCGGACTGTGACGAGTTGGCTGACCACATCGGCTTTCAGGA\n" +
            "GGCCTATCGGCGCTTCTACGGCCCGGTCTAGGGTGTCGCTCTGCTGGCCTGGCCGGCAACCCCAGTTCTGCTCCT\n" +
            "CTCCAGGCACCCTTCTTTCCTCTTCCCCTTGCCCTTGCCCTGACCTCCCAGCCCTATGGATGTGGGGTCCCCATC\n" +
            "ATCCCAGCTGCTCCCAAATAAACTCCAGAAG\n" +
            ">HSGLTH1 Human theta 1-globin gene\n" +
            "CCACTGCACTCACCGCACCCGGCCAATTTTTGTGTTTTTAGTAGAGACTAAATACCATATAGTGAACACCTAAGA\n" +
            "CGGGGGGCCTTGGATCCAGGGCGATTCAGAGGGCCCCGGTCGGAGCTGTCGGAGATTGAGCGCGCGCGGTCCCGG\n" +
            "GATCTCCGACGAGGCCCTGGACCCCCGGGCGGCGAAGCTGCGGCGCGGCGCCCCCTGGAGGCCGCGGGACCCCTG\n" +
            "GCCGGTCCGCGCAGGCGCAGCGGGGTCGCAGGGCGCGGCGGGTTCCAGCGCGGGGATGGCGCTGTCCGCGGAGGA\n" +
            "CCGGGCGCTGGTGCGCGCCCTGTGGAAGAAGCTGGGCAGCAACGTCGGCGTCTACACGACAGAGGCCCTGGAAAG\n" +
            "GTGCGGCAGGCTGGGCGCCCCCGCCCCCAGGGGCCCTCCCTCCCCAAGCCCCCCGGACGCGCCTCACCCACGTTC\n" +
            "CTCTCGCAGGACCTTCCTGGCTTTCCCCGCCACGAAGACCTACTTCTCCCACCTGGACCTGAGCCCCGGCTCCTC\n" +
            "ACAAGTCAGAGCCCACGGCCAGAAGGTGGCGGACGCGCTGAGCCTCGCCGTGGAGCGCCTGGACGACCTACCCCA\n" +
            "CGCGCTGTCCGCGCTGAGCCACCTGCACGCGTGCCAGCTGCGAGTGGACCCGGCCAGCTTCCAGGTGAGCGGCTG\n" +
            "CCGTGCTGGGCCCCTGTCCCCGGGAGGGCCCCGGCGGGGTGGGTGCGGG GGGCGTGCGGGGCGGGTGCAGGCGAG\n" +
            "TGAGCCTTGAGCGCTCGCCGCAGCTCCTGGGCCACTGCCTGCTGGTAACCCTCGCCCGGCACTACCCCGGAGACT\n" +
            "TCAGCCCCGCGCTGCAGGCGTCGCTGGACAAGTTCCTGAGCCACGTTATCTCGGCGCTGGTTTCCGAGTACCGCT\n" +
            "GAACTGTGGGTGGGTGGCCGCGGGATCCCCAGGCGACCTTCCCCGTGTTTGAGTAAAGCCTCTCCCAGGAGCAGC\n" +
            "CTTCTTGCCGTGCTCTCTCGAGGTCAGGACGCGAGAGGAAGGCGC";

}
