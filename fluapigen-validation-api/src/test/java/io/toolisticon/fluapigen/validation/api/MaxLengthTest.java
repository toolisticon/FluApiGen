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
    public void doValidationTest_multipleValuesInArray () {

        MaxLength.ValidatorImpl unit = new MaxLength.ValidatorImpl(5);

        final String[] matchingArray = {"", "abc","abcde"};
        final String[] nonMatchingArray = {"", "abc", "adsada","abcde"};
        final String[] matchingArrayWithNullValue = {"abc", null};

        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((String[])null));
        MatcherAssert.assertThat("Expect to return true in case of null element value", unit.validate(matchingArrayWithNullValue));


    }


    @Test
    public void doValidationTest_multipleValuesInIterable () {

        MaxLength.ValidatorImpl unit = new MaxLength.ValidatorImpl(5);

        final Iterable<String> matchingIterable = Arrays.asList("", "abc","abcde");
        final Iterable<String> nonMatchingIterable = Arrays.asList("", "abc", "adsada","abcde");
        final Iterable<String> matchingIterableWithNullValue = Arrays.asList("abc", null);

        MatcherAssert.assertThat("Expect to match", unit.validate(matchingIterable));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingIterable));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((Iterable<String>)null));
        MatcherAssert.assertThat("Expect to return true in case of null element value", unit.validate(matchingIterableWithNullValue));


    }

}
