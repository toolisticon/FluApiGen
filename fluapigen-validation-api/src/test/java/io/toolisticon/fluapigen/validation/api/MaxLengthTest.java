package io.toolisticon.fluapigen.validation.api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Unit test for {@link MaxLength}.
 */
public class MaxLengthTest {

    @Test
    public void doValidationTest_singleValue () {

        MaxLength.ValidatorImpl unit = new MaxLength.ValidatorImpl(5);

        MatcherAssert.assertThat("Expect to match", unit.validate(""));
        MatcherAssert.assertThat("Expect to match", unit.validate("abcde"));
        MatcherAssert.assertThat("Expect not to match", !unit.validate("someNotMatchingText"));
        MatcherAssert.assertThat("Expect not to match", !unit.validate("123456"));
        MatcherAssert.assertThat("Expect to return true in case of null ", unit.validate((String)null));

    }

    @Test
    public void doValidationTest_multipleValues () {

        MaxLength.ValidatorImpl unit = new MaxLength.ValidatorImpl(5);

        final String[] matchingArray = {"", "abc","abcde"};
        final String[] nonMatchingArray = {"", "abc", "adsada","abcde"};
        final String[] matchingArrayWithNullValue = {"abc", null};

        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((String[])null));
        MatcherAssert.assertThat("Expect to return true in case of null element value", unit.validate(matchingArrayWithNullValue));


    }

}
