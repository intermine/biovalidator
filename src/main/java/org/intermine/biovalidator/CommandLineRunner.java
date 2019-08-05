package org.intermine.biovalidator;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.intermine.biovalidator.api.ErrorMessage;
import org.intermine.biovalidator.api.Message;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.intermine.biovalidator.utils.BioValidatorUtils;
import org.intermine.biovalidator.validator.ValidatorType;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Command Line Runner for 'biovalidator'
 * @author deepak
 */

final class CommandLineRunner
{
    private static final PrintWriter WRITER;
    private static final String BASE_DOCS_URL = "/validator_rules/";
    private static final String FASTA_DOC_FILE_NAME = "Fasta-Validator.md";
    private static final String GFF3_DOC_FILE_NAME = "GFF3-Validator.md";
    private static final String FASTA = "fasta";
    private static final String GFF = "gff";
    private static final String GFF3 = "gff3";

    static {
        WRITER = new PrintWriter(System.out, true);
    }
    private CommandLineRunner() { }

    /**
     * run test
     * @param args Arguments
     */
    public static void main(String[] args) {

        /*args = Arrays.asList("-f",
        "/home/deepak/Documents/Intermine/FILES/CSV/Drosophila_2.na25.annot.csv",
        "-w").toArray(new String[]{});*/

        try {
            CommandLine commandLine = new CommandLine(new BioValidatorCommand());
            commandLine.parseArgs(args);
            if (commandLine.isUsageHelpRequested()) {
                commandLine.usage(System.out);
                return;
            }
            if (commandLine.isVersionHelpRequested()) {
                commandLine.printVersionHelp(System.out);
                return;
            }
            BioValidatorCommand command = CommandLine.populateCommand(
                new BioValidatorCommand(), args);

            if (command.isDocsEnabled()) { //print docs if requested
                printDocsFor(command.getDocs());
                return;
            }

            //check --file arg is provided or not, as it is a required argument
            if (!ArrayUtils.contains(args, "-f")) {
                WRITER.println("Missing required option '--file=<filename>'");
                return;
            }
            if (StringUtils.isBlank(command.getFilename())) { //check file is provided or not
                WRITER.println("Missing required parameter for option '--file' (<filename>)");
                return;
            }
            String file = command.getFilename();
            String validatorType = command.getValidatorType();

            WRITER.println("Validating " + getValidatorTypeName(file, validatorType) + " file...");

            ValidatorBuilder builder = ValidatorBuilder
                    .withFile(file, validatorType); //strict validation by-default

            if (command.isContinueOnError()) {
                builder.disableStopAtFirstError();
            }
            if (command.isDisableErrors()) {
                builder.disableErrors();
            }
            if (command.isEnableWarning()) { //enable warning
                builder.enableWarnings();
            }
            if (command.isPermissive()) { //allow permissive validation
                builder.disableStrictValidation();
            }

            ValidationResult result = builder.build().validate();
            WRITER.print("Result : ");
            if (result.isValid()) {
                WRITER.println("Valid File!!");
                displayMessages("Warning", result.getWarningMessages());
            } else {
                WRITER.println("Invalid File!!");
                displayMessages("Error", result.getErrorMessages());
                if (!result.getWarningMessages().isEmpty()) {
                    displayMessages("Warning", result.getWarningMessages());
                }
            }
        } catch (RuntimeException e) {
            WRITER.println(e.getMessage());
        }
        WRITER.close();
    }

    /**
     * returns validator verbose name form validator type, or guess from filename
     * @return validator name
     */
    private static String getValidatorTypeName(String filename, String validatorType) {
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(
                filename, validatorType);
        return opt.isPresent() ? opt.get().getName(): StringUtils.EMPTY;
    }

    private static void printDocsFor(String docs) {
        String filename = StringUtils.EMPTY;
        if (FASTA.equalsIgnoreCase(docs)) {
            filename = FASTA_DOC_FILE_NAME;
        } else if (StringUtils.equalsAnyIgnoreCase(docs, GFF, GFF3)) {
            filename = GFF3_DOC_FILE_NAME;
        } else {
            WRITER.println("Invalid docs type, argument must be one of(fasta, gff3)");
            return;
        }
        InputStream inputStream = CommandLineRunner.class
                .getResourceAsStream(BASE_DOCS_URL + filename);
        printInputStreamToConsole(inputStream);
    }

    private static void printInputStreamToConsole(InputStream inputStream) {
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream))) {
            bufferedReader.lines()
                    .forEach(System.out::println);
        } catch (IOException e) {
            WRITER.println("Unable to show documentation!");
        }
    }

    private static void displayMessages(String messageType, List<Message> messages) {
        if (messages.size() > 0) {
            WRITER.println(messageType + ((messages.size() < 2) ? ":" : "s:"));
            for (int i = 0; i < messages.size(); i++) {
                Message message = messages.get(i);
                if (message instanceof ErrorMessage) {
                    WRITER.println("\t" + (i + 1) + ". " + message.getMessage());
                } else {
                    WRITER.println("\t" + (i + 1) + ". " + message.getMessage());
                }
            }
        }
    }

    /**
     * Command Line Parser
     * @author deepak
     */
    @CommandLine.Command(name = "biovalidator", version = "1.0",
        mixinStandardHelpOptions = true, description = "validates biological file formats")
    static final class BioValidatorCommand implements Runnable
    {
        /**
         * Represent possible type of validator accepted by the BioValidatorCommand
         */
        static class ValidatorTypes extends ArrayList<String>
        {
            /** create possible Validator
             */
            ValidatorTypes() {
                super(Arrays.asList("", "fasta", "fasta-dna", "fasta-rna", "fasta-protein"));
            }
        }

        @CommandLine.Option(
                names = {"-t", "--type"},
                description = "ValidatorType, fasta is used as default if not provided\n"
                        + "possible values:\n fasta,\n fasta-dna,\n fasta-rna,\n fasta-protein,\n"
                        + " gff3,\n csv",
                defaultValue = "",
                completionCandidates = ValidatorTypes.class)
        private String validatorType;

        @CommandLine.Option(names = {"-f", "--file"},
            description = "file to be validated",
            required = false)
        private String filename;

        @CommandLine.Option(names = {"-d", "--disable-errors"}, description = "disable errors")
        private boolean disableErrors;

        @CommandLine.Option(names = {"-w", "--enable-warnings"}, description = "enable warnings")
        private boolean enableWarning;

        @CommandLine.Option(names = {"-p", "--permissive"}, description = "permissive validation")
        private boolean permissive;

        @CommandLine.Option(names = {"-z", "--continue-on-error"},
            description = "continue validation if error encountered")
        private boolean continueOnError;

        @CommandLine.Option(names = {"-m", "--docs"},
                description = "documentation, ex: --docs fasta")
        private String docs;


        private BioValidatorCommand() {
        }

        @Override public void run() {

        }
        /**
         * Gets disableErrors.
         *
         * @return Value of disableErrors.
         */
        boolean isDisableErrors() {
            return disableErrors;
        }

        /**
         * Gets filename.
         *
         * @return Value of filename.
         */
        String getFilename() {
            return filename;
        }

        /**
         * Gets isPermissive.
         *
         * @return Value of isPermissive.
         */
        boolean isPermissive() {
            return permissive;
        }

        /**
         * Gets disableWarning.permissive
         *
         * @return Value of disableWarning.
         */
        boolean isEnableWarning() {
            return enableWarning;
        }

        /**
         * Gets continueOnError.
         *
         * @return Value of continueOnError.
         */
        public boolean isContinueOnError() {
            return continueOnError;
        }

        /**
         * Gets validatorType.
         *
         * @return Value of validatorType.
         */
        public String getValidatorType() {
            return validatorType;
        }

        /**
         * Return whether docs is enabled or not
         * @return boolean
         */
        public boolean isDocsEnabled() {
            return StringUtils.isNotBlank(docs);
        }

        /**
         * Gets validatorType.
         *
         * @return Value of validatorType.
         */
        public String getDocs() {
            return docs;
        }
    }
}
