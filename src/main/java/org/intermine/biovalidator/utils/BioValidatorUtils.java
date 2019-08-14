package org.intermine.biovalidator.utils;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */
import org.apache.commons.lang3.math.NumberUtils;
import org.intermine.biovalidator.validator.ValidatorType;

import java.io.File;
import java.util.Optional;

/**
 * This class provides some commonly utility methods
 * @author deepak kumar
 */
public final class BioValidatorUtils
{
    private static final String BOOLEAN_TRUE = "true";
    private static final String BOOLEAN_FALSE = "false";

    private BioValidatorUtils() { }

    /**
     * Get validator type from string or guess validator type from filename
     * @param filename file to be validated
     * @param validatorType validator type
     * @return optional of validator type
     */
    public static Optional<ValidatorType> getOrGuessValidatorType(String filename,
                                                                  String validatorType) {
        try {
            if (validatorType != null) {
                validatorType = validatorType.trim();
            }
            return Optional.of(ValidatorType.of(validatorType));
        } catch (IllegalArgumentException e) {
            // try to guess validator type from given filename
            return guessValidatorType(filename);
        }
    }

    /**
     * Guess validator type from filename(i.e. filename's extension) if available
     * @param filename absolute file path
     * @return optional of validator type
     */
    private static Optional<ValidatorType> guessValidatorType(String filename) {
        Optional<String> extensionOpt = getFileExtension(filename);
        if (!extensionOpt.isPresent()) {
            return Optional.empty();
        }
        String foundExtension = extensionOpt.get();
        //try to match found extension with supported-extensions of all available validator types
        for (ValidatorType validatorType : ValidatorType.values()) {
            if (validatorType.getSupportedFileExtensions().contains(foundExtension)) {
                return Optional.of(validatorType);
            }
        }
        return Optional.empty();
    }

    /**
     * Gets extension of a file
     * @param filename filename
     * @return optional of file extension
     */
    public static Optional<String> getFileExtension(String filename) {
        int extensionIndex = filename.lastIndexOf(".");
        int separatorIndex = filename.lastIndexOf(File.separator);
        if (extensionIndex == -1 || extensionIndex < separatorIndex) {
            return Optional.empty();
        }
        return Optional.of(filename.substring(extensionIndex + 1));
    }

    /**
     * Tests whether a string is a float or not
     * @param s string value
     * @return boolean, whether given string an float or not
     */
    public static boolean isFloat(String s) {
        return NumberUtils.isCreatable(s);
    }

    /**
     * Tests whether a string is an integer or not
     * @param s string value
     * @return boolean, whether given string an integer or not
     */
    public static boolean isInteger(String s) {
        final int radix = 10;
        if (s.isEmpty()) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) {
                    return false;
                }
                else {
                    continue;
                }
            }
            if (Character.digit(s.charAt(i), radix) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Tests whether a string is a boolean or not
     * @param s string value
     * @return boolean, whether given string a boolean or not
     */
    public static boolean isBoolean(String s) {
        if (s == null) {
            return false;
        }
        return BOOLEAN_TRUE.equalsIgnoreCase(s) || BOOLEAN_FALSE.equalsIgnoreCase(s);
    }
}
