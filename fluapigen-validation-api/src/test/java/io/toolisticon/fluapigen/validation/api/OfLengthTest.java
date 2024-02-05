package io.toolisticon.fluapigen.validation.api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Unit test for {@link MaxLength}.
 */
public class OfLengthTest {

    @Test
    public void doValidationTest_singleValue() {

        OfLength.ValidatorImpl unit = new OfLength.ValidatorImpl(2);

        MatcherAssert.assertThat("Expect not to match", !unit.validate(""));
        MatcherAssert.assertThat("Expect not to match", !unit.validate("1"));
        MatcherAssert.assertThat("Expect not to match", !unit.validate("abc"));
        MatcherAssert.assertThat("Expect to match", unit.validate("12"));
        MatcherAssert.assertThat("Expect to return true in case of null ", unit.validate((String) null));

    }

    @Test
    public void doValidationTest_multipleValues() {

        OfLength.ValidatorImpl unit = new OfLength.ValidatorImpl(2);

        final String[] matchingArray = {"12", "21", "22"};
        final String[] nonMatchingArray = {"12", "21", "22", "abcde"};
        final String[] matchingArrayWithNullValue = {"12", null};

        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((String[]) null));
        MatcherAssert.assertThat("Expect to return true in case of null element value", unit.validate(matchingArrayWithNullValue));


    }

}
