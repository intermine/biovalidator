package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.assertTrue;

public class GffValidatorFeatureLineTest extends BaseValidatorTest {

	//@Test
	public void testGff3Feature() throws FileNotFoundException, ValidationFailureException {
		String filePath = getFullPath("gff3/valid/sample.gff3");
		FileReader reader = new FileReader(filePath);

		Validator validator = ValidatorBuilder.ofType(new Gff3Validator(reader)).build();
		validator.validate(result -> {
			if (!result.isValid()) {
				result.getErrorMessages().forEach(System.out::println);
			}
			assertTrue(result.isValid());
		});
	}

	//@Test
	public void testValidGff3() throws FileNotFoundException, ValidationFailureException {
		String filePath = getFullPath("gff3/valid/gff3_spec_example.gff3");
		FileReader reader = new FileReader(filePath);

		Validator validator = ValidatorBuilder.ofType(new Gff3Validator(reader)).build();
		validator.validate(result -> {
			if (!result.isValid()) {
				result.getErrorMessages().forEach(System.out::println);
			}
			assertTrue(result.isValid());
		});
	}

}
