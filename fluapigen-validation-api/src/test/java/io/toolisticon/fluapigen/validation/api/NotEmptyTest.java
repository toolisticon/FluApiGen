package io.toolisticon.fluapigen.validation.api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Unit test for {@link MinLength}.
 */
public class NotEmptyTest {

    @Test
    public void doValidationTest_singleValue() {

        NotEmpty.ValidatorImpl unit = new NotEmpty.ValidatorImpl();

        MatcherAssert.assertThat("Expect not to match", !unit.validate(""));
        MatcherAssert.assertThat("Expect to match", unit.validate("abcde"));
        MatcherAssert.assertThat("Expect to match", unit.validate("someNotMatchingText"));
        MatcherAssert.assertThat("Expect to return true in case of null ", unit.validate((String) null));

    }

    @Test
    public void doValidationTest_multipleValues() {

        NotEmpty.ValidatorImpl unit = new NotEmpty.ValidatorImpl();

        final String[] nonMatchingArray = {"", "abc", "abcde"};
        final String[] matchingArray = {"adsada", "gdgugjvhkbhkjbh"};
        final String[] matchingArrayWithNullValue = {"abcde", null};

        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((String[]) null));
        MatcherAssert.assertThat("Expect to return true in case of null element value", unit.validate(matchingArrayWithNullValue));


    }

}