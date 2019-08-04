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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents all the validator supported by biovalidator.
 */
public enum ValidatorType {
    /**
     * Represents a validator of type FASTA
     */
    FASTA("fasta", Arrays.asList("fa", "fasta")),

    /**
     * Represents validators of type FASTA with specific sequence
     */
    FASTA_DNA("fasta-dna"), FASTA_RNA("fasta-rna"), FASTA_PROTEIN("fasta-protein"),

    /**
     * Represents a validator of type GFF3
     */
    GFF("gff", Collections.singletonList("gff")), GFF3("gff3",  Arrays.asList("gff", "gff3")),

    /**
     * Represents a validator of type CSV/TSV
     */
    CSV("csv",  Arrays.asList("csv", "tsv"));

    private String name;
    private List<String> supportedFileExtensions;

    /**
     * Construct a validator type with its verbose name
     * @param name name of the validator type
     */
    ValidatorType(String name) {
        this(name, Collections.emptyList());
    }

    /**
     * Create a ValidatorType with supported file extensions
     * @param name validator type
     * @param supportedFileExtensions supported extensions
     */
    ValidatorType(String name, List<String> supportedFileExtensions) {
        this.name = name;
        this.supportedFileExtensions = supportedFileExtensions;
    }

    /**
     * return ValidatorType from string name (case-insensitive)
     * @param str type of validator
     * @return instance of ValidatorType
     */
    public static ValidatorType of(String str) {
        for (ValidatorType type: ValidatorType.values()) {
            if (type.getName().equalsIgnoreCase(str)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid validator type " + str);
    }

    /**
     * Gets name the validator
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets all supported extensions for a validator type
     * @return list of supported extensions
     */
    public List<String> getSupportedFileExtensions() {
        return supportedFileExtensions;
    }
}
