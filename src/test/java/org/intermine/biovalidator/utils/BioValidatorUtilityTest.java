package org.intermine.biovalidator.utils;

import org.intermine.biovalidator.utils.BioValidatorUtils;
import org.intermine.biovalidator.validator.ValidatorType;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class BioValidatorUtilityTest {

    @Test
    public void testValidatorTypeParser() {
        String filename = "fasta.fa";
        String validatorType = "fasta  ";
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(filename, validatorType);
        assertTrue(opt.isPresent());
        assertEquals(ValidatorType.FASTA, opt.get());
    }

    @Test
    public void testValidatorTypeParserWithWrongValidatorType() {
        String filename = "fasta.fa";
        String validatorType = "my_custom_type";
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(filename, validatorType);
        assertTrue(opt.isPresent());
        assertEquals(ValidatorType.FASTA, opt.get());
    }

    @Test
    public void testValidatorTypeParserWithoutValidatorType() {
        String filename = "file.gff";
        String validatorType = null;
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(filename, validatorType);
        assertTrue(opt.isPresent());
        assertEquals(ValidatorType.GFF, opt.get());
    }

    @Test
    public void testValidatorTypeParserWithoutGff3Type() {
        String filename = "file.gff3";
        String validatorType = "wrong_type";
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(filename, validatorType);
        assertTrue(opt.isPresent());
        assertEquals(ValidatorType.GFF3, opt.get());
    }

    @Test
    public void testValidatorTypeParserWithExplicitTypeAsCSV() {
        String filename = "file.gff3";
        String validatorType = "csv";
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(filename, validatorType);
        assertTrue(opt.isPresent());
        assertEquals(ValidatorType.CSV, opt.get());
    }

    @Test
    public void testValidatorTypeParserWithExplicitTypeAsTSV() {
        String filename = "file.tsv";
        String validatorType = "csadaskdjakslv";
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(filename, validatorType);
        assertTrue(opt.isPresent());
        assertEquals(ValidatorType.CSV, opt.get());
    }

    @Test
    public void testValidatorTypeParserWithWrongTypeAndFileExtension() {
        String filename = "file.custom_type";
        String validatorType = "custom_type";
        Optional<ValidatorType> opt = BioValidatorUtils.getOrGuessValidatorType(filename, validatorType);
        assertFalse(opt.isPresent());
    }
}
