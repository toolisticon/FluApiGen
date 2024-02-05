package io.toolisticon.fluapigen.integrationtest;

import io.toolisticon.fluapigen.validation.api.ValidatorException;
import org.junit.Test;

public class ValidatorExampleTest {

    @Test(expected = RuntimeException.class)
    public void testValidator_failingValidation() {

        ValidatorExampleStarter.setName("bbbadadadsd").myCommand();

    }

    @Test()
    public void testValidator() {

        ValidatorExampleStarter.setName("aaaBDSXS").myCommand();
        ValidatorExampleStarter.setNameWithNullableOnParameter(null).myCommand();
        ValidatorExampleStarter.setNameWithNullableOnMethod("aaaBDSXS").myCommand();

    }

    @Test(expected = ValidatorException.class)
    public void testOverruledNotNullAtParameter() {
        ValidatorExampleStarter.setNameWithNullableOnMethod(null).myCommand();
    }

}
