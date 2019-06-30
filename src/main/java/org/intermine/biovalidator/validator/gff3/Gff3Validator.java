package org.intermine.biovalidator.validator.gff3;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.ValidationResult;
import org.intermine.biovalidator.parser.Gff3FeatureParser;
import org.intermine.biovalidator.validator.AbstractValidator;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author deepak
 */
public class Gff3Validator extends AbstractValidator
{
    private InputStreamReader inputStreamReader;

    /**
     * Contruct a Gff3Validator with an input stream
     * @param inputStreamReader input source
     */
    public Gff3Validator(InputStreamReader inputStreamReader) {
        this.inputStreamReader = inputStreamReader;
    }

    @Nonnull
    @Override
    public ValidationResult validate() throws ValidationFailureException {
        try (Parser<Feature> parser = new Gff3FeatureParser(inputStreamReader)) {
            Feature feature = parser.parseNext();
            while (feature != null) {

                feature = parser.parseNext();
            }
            return validationResult;
        } catch (IOException e) {
            throw new ValidationFailureException(e.getMessage());
        }
    }
}
