package org.intermine.biovalidator.validator.fasta.sequencevalidator;

/*
 * Copyright (C) 2002-2019 FlyMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

/**
 * A generic sequence validator to test whether a sequence
 * is following IUPAC codes or not.
 * @author deepak
 */
public class GenericSequenceValidator extends AbstractSequenceValidator
{
    @Override
    public boolean isValidLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
                || (c == '-') || (c == '*') || (c == '.') || Character.isWhitespace(c);
    }
}
