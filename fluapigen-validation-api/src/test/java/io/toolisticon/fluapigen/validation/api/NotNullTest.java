package io.toolisticon.fluapigen.validation.api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit test for {@link NotNull}.
 */
public class NotNullTest {

    @Test
    public void doValidationTest_singleValue() {

        NotNull.ValidatorImpl unit = new NotNull.ValidatorImpl();

        MatcherAssert.assertThat("Expect to match", unit.validate("123"));
        MatcherAssert.assertThat("Expect not to match", !unit.validate((String) null));

    }

    @Test
    public void doValidationTest_multipleValuesInArray() {

        NotNull.ValidatorImpl unit = new NotNull.ValidatorImpl();

        final String[] matchingArray = {"tobias.stamann@holisticon.de", "tobias.stamann@holisticon.de"};
        final String[] nonMatchingArray = {"tobias.stamann@holisticon.de", null};


        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return false in case of null value", !unit.validate((String[]) null));


    }

    @Test
    public void doValidationTest_multipleValuesinIterable() {

        NotNull.ValidatorImpl unit = new NotNull.ValidatorImpl();

        final Iterable<String> matchingIterable = Arrays.asList("tobias.stamann@holisticon.de", "tobias.stamann@holisticon.de");
        final Iterable<String> nonMatchingIterable = Arrays.asList("tobias.stamann@holisticon.de", null);


        MatcherAssert.assertThat("Expect to match", unit.validate(matchingIterable));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingIterable));
        MatcherAssert.assertThat("Expect to return false in case of null value", !unit.validate((Iterable<String>) null));


    }

}
