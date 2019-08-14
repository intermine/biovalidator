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

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *  Processor for Sequence-Ontology file
 * @author deepak
 */
public final class SequenceOntologyFileProcessor
{
    private SequenceOntologyFileProcessor() { }

    /**
     * parse SO file and extract all the Sequence-Ontology 'name' and 'id' from it and write all
     * the extracted SO 'name' and 'id' into a separate file named 'processed_so_terms.obo'.
     * @param args cmd-args
     * @throws IOException if parsing failed
     */
    public static void main(String...args) throws IOException {
        String basePath = "src/main/resources/";

        String sequenceOntolofgyFilename = basePath + "gff3/so-simple.obo";
        String processFilePath = basePath + "gff3/processed_so_terms.obo";

        BufferedReader reader = new BufferedReader(new FileReader(sequenceOntolofgyFilename));
        BufferedWriter writer = new BufferedWriter(new FileWriter(processFilePath));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[Typedef]")) {
                    break; // END of SO-Term definition reached
                }
                if (line.startsWith("id: ") || line.startsWith("name: ")) {
                    String[] lineValues = line.trim().split(" ");
                    if (lineValues.length > 1 && StringUtils.isNotBlank(lineValues[1])) {
                        writer.write(lineValues[1]);
                        writer.newLine();
                    }
                }
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
