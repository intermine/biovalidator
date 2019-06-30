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
import org.intermine.biovalidator.validator.gff3.Feature;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author deepak
 */
public class Gff3FeatureParser implements Parser<Feature>
{
    private GenericLineByLineParser lineParser;

    /**
     * Construct Gff3 feature parse with an input source
     * @param inputStreamReader input source
     */
    public Gff3FeatureParser(InputStreamReader inputStreamReader) {
        this.lineParser = new GenericLineByLineParser(inputStreamReader);
    }
    @Override
    public Feature parseNext() throws ParsingException {
        return null;
    }

    @Override
    public void close() throws IOException {
        lineParser.close();
    }
}
