package io.toolisticon.fluapigen.integrationtest;

import org.junit.Test;

public class ValidatorExampleTest {

    @Test(expected = RuntimeException.class)
    public void testValidator_failingValidation() {

        ValidatorExampleStarter.setName("bbbadad").myCommand();

    }
}
