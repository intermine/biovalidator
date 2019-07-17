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

import org.apache.commons.lang3.StringUtils;
import org.intermine.biovalidator.api.Parser;
import org.intermine.biovalidator.api.ParsingException;
import org.intermine.biovalidator.validator.gff3.FeatureLine;
import org.intermine.biovalidator.validator.gff3.Gff3CommentLine;
import org.intermine.biovalidator.validator.gff3.Gff3Line;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author deepak
 */
public class Gff3FeatureParser implements Parser<Optional<Gff3Line>>
{
    private GenericLineByLineParser lineParser;
    private long totalLineCount;
    /**
     * Construct Gff3 feature parse with an input source
     * @param inputStreamReader input source
     */
    public Gff3FeatureParser(InputStreamReader inputStreamReader) {
        this.lineParser = new GenericLineByLineParser(inputStreamReader);
    }
    @Override
    public Optional<Gff3Line> parseNext() throws ParsingException {
        String line = lineParser.parseNext();
        totalLineCount++;
        if (line != null) {
            if (line.startsWith("#")) {
                return Optional.of(Gff3CommentLine.of(line));
            } else {
                return Optional.of(parseFeature(line, totalLineCount));
            }
        }
        return Optional.empty();
    }

    @Override
    public void close() throws IOException {
        lineParser.close();
    }

    private FeatureLine parseFeature(String featureStr, long totalLineCount)
            throws ParsingException {
        String[] columns = featureStr.split("\t"); //split only on tab not on space

        if (columns.length < 9) {
            String msg = "Unable to parse! a feature must have 9 columns at line " + totalLineCount;
            throw new ParsingException(msg);
        }

        String seqId = columns[0];
        String source = columns[1];
        String type = columns[2];

        String startCord = columns[3].trim();
        String endCord = columns[4].trim();

        String score = columns[5].trim();
        /*double scoreValue = 0.0D;
        if (NumberUtils.isParsable(score)) {
            scoreValue = Double.parseDouble(score);
        }*/

        String strand = columns[6];
        String phase = columns[7];
        String attributes = columns[8];

        Map<String, String> keyValAttributeMapping = parseAttributes(attributes);

        return new FeatureLine(seqId, source, type, startCord, endCord, score, strand, phase,
                attributes, keyValAttributeMapping);
    }

    /**
     * Parse GFF3 attributes in a key-value pair mapping
     * @param attributes attribute string
     * @return Map attributes Key-Value
     */
    private Map<String, String> parseAttributes(String attributes) {
        Map<String, String> attributeMapping = new HashMap<>();
        for (String attr: attributes.split(";")) {
            if (attr.contains("=")) {
                String[] attrPair = attr.trim().split("=");
                String value = attrPair.length > 1 ? attrPair[1]: StringUtils.EMPTY;
                attributeMapping.put(attrPair[0], value);
            } else if (StringUtils.isNotBlank(attr)) {
                attributeMapping.put(attr, StringUtils.EMPTY);
            }
        }
        return attributeMapping;
    }
}
