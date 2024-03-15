package io.toolisticon.fluapigen.integrationtest;

import io.toolisticon.fluapigen.validation.api.ValidatorException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
        ValidatorExampleStarter.setVariousNamesWithNullableOnMethod("aaaBDSXS", "aaaBDNNN").myCommand();
        ValidatorExampleStarter.setVariousNamesWithNullableOnMethod(Arrays.asList("aaaBDSXS", "aaaBDNNN")).myCommand();
        ValidatorExampleStarter.addClass(ValidatorExampleTest.class).myCommand();
        ValidatorExampleStarter.addClasses(ValidatorExampleTest.class).myCommand();
        ValidatorExampleStarter.addClasses(Arrays.asList(ValidatorExampleTest.class)).myCommand();
    }

    @Test(expected = ValidatorException.class)
    public void testOverruledNotNullAtParameter() {
        ValidatorExampleStarter.setNameWithNullableOnMethod(null).myCommand();
    }

    @Test(expected = ValidatorException.class)
    public void testOverruledNotNullAtParameter_List() {
        ValidatorExampleStarter.setVariousNamesWithNullableOnMethod((List<String>)null).myCommand();
    }

    @Test(expected = ValidatorException.class)
    public void testOverruledNotNullAtParameter_Array() {
        ValidatorExampleStarter.setVariousNamesWithNullableOnMethod((String)null).myCommand();
    }


    static class NoNoargConstructor {
        NoNoargConstructor (String arg) {

        }
    }

    @Test(expected = ValidatorException.class)
    public void testHasNoargConstructor_failing_singleValue() {
        ValidatorExampleStarter.addClass(NoNoargConstructor.class).myCommand();
    }


    @Test(expected = ValidatorException.class)
    public void testHasNoargConstructor_failing_arrayValue() {
        ValidatorExampleStarter.addClasses(NoNoargConstructor.class).myCommand();
    }

    @Test(expected = ValidatorException.class)
    public void testHasNoargConstructor_failing_iterableValue() {
        ValidatorExampleStarter.addClasses(Arrays.asList(NoNoargConstructor.class)).myCommand();
    }

}
