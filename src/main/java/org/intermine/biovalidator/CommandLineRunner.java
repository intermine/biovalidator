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

import org.apache.commons.lang3.StringUtils;
import org.intermine.biovalidator.api.ErrorMessage;
import org.intermine.biovalidator.api.Message;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorBuilder;
import picocli.CommandLine;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command Line Runner for 'biovalidator'
 * @author deepak
 */

final class CommandLineRunner
{
    private static final PrintWriter WRITER;

    static {
        WRITER = new PrintWriter(System.out, true);
    }
    private CommandLineRunner() { }

    /**
     * run test
     * @param args Arguments
     */
    public static void main(String[] args) {

        /*args = Arrays.asList("-t", "fasta-dna",
            "-f", "/home/deepak/Downloads/FASTA_FILES/protein.fa"
            , "-w-d").toArray(new String[]{});*/
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

            if (command.isDocsEnabled()) {
                printDocsFor(command.getDocs());
                return;
            }

            String validatorType = command.getValidatorType();

            /* set fasta as default validator type if not provided */
            validatorType = StringUtils.isBlank(validatorType) ? "fasta" : validatorType;

            WRITER.println("Validating " + validatorType + " file...");

            ValidatorBuilder builder = ValidatorBuilder
                    .withFile(command.getFilename(), validatorType); //strict validation by-default

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

    private static void printDocsFor(String docs) {
        String filename = StringUtils.EMPTY;
        if (docs.equalsIgnoreCase("fasta")) {

        } else if (StringUtils.equalsAnyIgnoreCase(docs, "gff", "gff3")) {

        } else if (StringUtils.equalsAnyIgnoreCase(docs, "csv", "tsv")) {

        } else {
            WRITER.println("Invalid docs type, argument must be one of(fasta, gff3, csv)");
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
            required = true)
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

        @CommandLine.Option(names = {"--docs"},
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
