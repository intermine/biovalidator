package org.intermine.biovalidator.validator.gff3;

import org.intermine.biovalidator.BaseValidatorTest;
import org.intermine.biovalidator.api.ValidationFailureException;
import org.intermine.biovalidator.api.Validator;
import org.intermine.biovalidator.api.ValidatorBuilder;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.Assert.assertTrue;

public class FastaValidatorFeatureTest extends BaseValidatorTest {

	@Test
	public void testGff3Feature() throws FileNotFoundException, ValidationFailureException {
		String filePath = getFullPath("gff3/valid/sample.gff3");
		FileReader reader = new FileReader(filePath);

		Validator validator = ValidatorBuilder.ofType(new Gff3Validator(reader)).build();
		/*validator.validate(result -> {
			assertTrue(result.isValid());
		});*/
	}

}
