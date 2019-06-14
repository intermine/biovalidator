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

import org.intermine.biovalidator.api.ErrorMessage;
import org.intermine.biovalidator.api.Message;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.api.ValidatorHelper;
import org.intermine.biovalidator.utils.StringUtils;
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

        List<String> argList = Arrays.asList("-t", "fasta-dna",
                "-e", "-f", "/home/deepak/Documents/FASTA_FILES/protein.fa");

        try {
            BioValidatorCommand command = CommandLine.populateCommand(
                    new BioValidatorCommand(), args/*.toArray(new String[]{})*/);

            String validatorType = command.getValidatorType();

            /* set fasta as default validator type if not provided */
            validatorType = StringUtils.isBlank(validatorType) ? "fasta" : validatorType;
            WRITER.println("Validating " + validatorType + " file...");

            ValidationResult result;
            switch (validatorType) {
                case "fasta":
                    result = ValidatorHelper.validateFasta(command.getFilename());
                    break;
                case "fasta-dna":
                case "fasta-rna":
                    result = ValidatorHelper.validateFastaDna(command.getFilename());
                    break;
                case "fasta-protein":
                    result = ValidatorHelper.validateFastaProtein(command.getFilename());
                    break;
                default:
                    throw new IllegalArgumentException("invalid validator type");
            }
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
        } catch (ValidationFailureException |  RuntimeException e) {
            WRITER.println(e.getMessage());
        }
        WRITER.close();
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
    static final class BioValidatorCommand
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
                names = "-t",
                description = "ValidatorType",
                defaultValue = "",
                completionCandidates = ValidatorTypes.class)
        private String validatorType;

        @CommandLine.Option(names = "-f", description = "file to be validated", required = true)
        private String filename;

        @CommandLine.Option(names = "-e", description = "show errors")
        private boolean showError;

        @CommandLine.Option(names = "-w", description = "show warnings")
        private boolean showWarning;

        @CommandLine.Option(names = "-s", description = "strict validation")
        private boolean isStrict;

        @CommandLine.Option(names = "-p", description = "permissive validation")
        private boolean isPermissive;


        private BioValidatorCommand() {
            this.showWarning = true;
            this.showError = true;
        }


        /**
         * Gets isPermissive.
         *
         * @return Value of isPermissive.
         */
        boolean isIsPermissive() {
            return isPermissive;
        }

        /**
         * Gets showError.
         *
         * @return Value of showError.
         */
        boolean isShowError() {
            return showError;
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
         * Gets isStrict.
         *
         * @return Value of isStrict.
         */
        boolean isIsStrict() {
            return isStrict;
        }

        /**
         * Gets showWarning.
         *
         * @return Value of showWarning.
         */
        boolean isShowWarning() {
            return showWarning;
        }

        /**
         * Gets validatorType.
         *
         * @return Value of validatorType.
         */
        String getValidatorType() {
            return validatorType;
        }
    }
}
