package org.intermine.biovalidator.validator;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.biovalidator.api.ValidationResult;

/**
 * A Rule validator
 * @param <T> type of object whose instance is to be validated
 * @author deepak
 */
public interface RuleValidator<T>
{
    /**
     * validate an object and add errors and warnings to validationResult is applicable
     * @param objectToBeValidated object of type T that needs to be validated
     * @param validationResult instance of validationResult to add errors and warnings
     * @param currentLineOfInput current line of the input
     * @return boolean representing whether object is valid or not
     */
    boolean validateAndAddError(T objectToBeValidated,
                                ValidationResult validationResult,
                                long currentLineOfInput);
}
