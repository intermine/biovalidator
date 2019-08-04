package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Ignore
public class Gff3PerformanceTest {

    private static void run() {
        //String file = "/home/deepak/Documents/FASTA_FILES/GFF/ref_GRCh38.p12_top_level.gff3";
        String file = "/home/deepak/Documents/FASTA_FILES/GFF/Homo_sapiens.GRCh38.97.gff3";
        //String file = "/home/deepak/Documents/FASTA_FILES/GFF/Homo_sapiens.GRCh38.97.chromosome.1.gff3";

        Validator validator = ValidatorBuilder.withFile(file, "gff3")
                //.enableWarnings()
                //.disableStopAtFirstError()
                .build();
        ValidationResult result = validator.validate();
        if (result.isValid()) {
            System.out.println("Valid File");
        } else {
            System.out.println("Invalid File!");
            result.getErrorMessages().forEach(System.out::println);
        }
        result.getWarningMessages().forEach(System.out::println);
    }

    @Test
    @Ignore
    public void test1GBGff3File() {
        simpleBenchmark(//String file = "/home/deepak/Documents/FASTA_FILES/GFF/ref_GRCh38.p12_top_level.gff3";
//String file = "/home/deepak/Documents/FASTA_FILES/GFF/Homo_sapiens.GRCh38.97.chromosome.1.gff3";
//.enableWarnings()
//.disableStopAtFirstError()
                Gff3PerformanceTest::run);
    }

    public static void simpleBenchmark(Runnable codeToBeValidated){
        long start = System.nanoTime();
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        codeToBeValidated.run();

        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        long timeTaken = System.nanoTime() - start;
        System.out.println("Time Taken : " +
                TimeUnit.MILLISECONDS.convert((timeTaken), TimeUnit.NANOSECONDS));
        System.out.print("Memory Used : " + (afterUsedMem-beforeUsedMem));
    }
}
