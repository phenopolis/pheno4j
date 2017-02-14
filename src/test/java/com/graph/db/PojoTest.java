package com.graph.db;

import java.util.List;

import org.junit.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.test.impl.DefaultValuesNullTester;
import com.openpojo.validation.test.impl.GetterTester;

public class PojoTest {
	  // Configured for expectation, so we know when a class gets added or removed.
	  private static final int EXPECTED_CLASS_COUNT = 16;

	  // The package to test
	  private static final String POJO_PACKAGE = "com.graph.db.domain.output";

	  @Test
	  public void ensureExpectedPojoCount() {
	    List <PojoClass> pojoClasses = PojoClassFactory.getPojoClasses(POJO_PACKAGE,
	                                                                   new FilterPackageInfo());
	    Affirm.affirmEquals("Classes added / removed?", EXPECTED_CLASS_COUNT, pojoClasses.size());
	  }

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

	    validator.validate(POJO_PACKAGE, new FilterPackageInfo());
	  }
	}