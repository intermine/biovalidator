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

/**
 * Represents all the validator supported by biovalidator.
 */
public enum ValidatorType {
    /**
     * type for each available validator and it type
     */
    FASTA("fasta"), FASTA_DNA("fasta-dna"), FASTA_RNA("fasta-rna"), FASTA_PROTEIN("fasta-protein"),

    /**
     * Represents a validator of type GFF3
     */
    GFF3("gff3"),

    /**
     * Represents a validator of type CSV/TSV
     */
    CSV("csv");

    private String name;

    /**
     * Construct a validator type with its verbose name
     * @param name name of the validator type
     */
    ValidatorType(String name) {
        this.name = name;
    }

    /**
     * return ValidatorType from string name
     * @param str type of validator
     * @return instance of ValidatorType
     */
    public static ValidatorType of(String str) {
        for (ValidatorType type: ValidatorType.values()) {
            if (type.getName().equals(str)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid validator type");
    }

    /**
     * Gets name the validator
     * @return name
     */
    public String getName() {
        return name;
    }
}
