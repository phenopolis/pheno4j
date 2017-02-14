package com.graph.db;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;

public class PojoTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PojoTest.class);
	
	// The packages to test
	private static final List<String> POJO_PACKAGE = Arrays.asList(
			"com.graph.db.domain.output",
			"com.graph.db.domain.input.annotation"
			);

	@Test
	public void testPojoStructureAndBehavior() {
		Validator validator = ValidatorBuilder.create()
	                          // Add Rules to validate structure for POJO_PACKAGE
	                          // See com.openpojo.validation.rule.impl for more ...
	                          .with(new GetterMustExistRule())
	                          // Add Testers to validate behaviour for POJO_PACKAGE
	                          // See com.openpojo.validation.test.impl for more ...
	                          .with(new GetterTester())
	                          .with(new DefaultValuesNullTester())
	                          .build();
		for (String pojoPackage : POJO_PACKAGE) {
			LOGGER.info("Testing package: {}", pojoPackage);
			validator.validate(pojoPackage, new FilterPackageInfo());
		}
	}
}