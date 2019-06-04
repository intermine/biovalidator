package org.intermine.biovalidator.parser;

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
import org.intermine.biovalidator.api.ParsingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author deepak
 */
public class GenericFastaParser implements Parser<String>
{
    private BufferedReader br;

    /**
     * Construct fasta parser with input source
     * @param reader input source
     */
    public GenericFastaParser(Reader reader) {
        this.br = new BufferedReader(reader);
    }

    /**
     * TODO
     * @return parsing result
     * @throws ParsingException if parsing failed
     */
    @Override
    public String parseNext() throws ParsingException {
        try {
            String line = br.readLine();
            if (line != null && line.charAt(0) == '>') {
                return br.readLine(); //skip header
            }
            return line;
        } catch (IOException e) {
            throw new ParsingException(e.getMessage());
        }
    }
}
