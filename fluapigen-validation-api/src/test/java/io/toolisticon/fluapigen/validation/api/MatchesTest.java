package io.toolisticon.fluapigen.validation.api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * Unit test for {@link Matches}.
 */
public class MatchesTest {

    @Test
    public void doValidationTest_singleValue () {

        Matches.ValidatorImpl unit = new Matches.ValidatorImpl(".*[@].*");

        MatcherAssert.assertThat("Expect to match", unit.validate("tobias.stamann@holisticon.de"));
        MatcherAssert.assertThat("Expect not to match", !unit.validate("someNotMatchingText"));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((String)null));

    }

    @Test
    public void doValidationTest_multipleValues () {

        Matches.ValidatorImpl unit = new Matches.ValidatorImpl(".*[@].*");

        final String[] matchingArray = {"tobias.stamann@holisticon.de", "tobias.stamann@holisticon.de"};
        final String[] nonMatchingArray = {"tobias.stamann@holisticon.de", "someNotMatchingText"};
        final String[] matchingArrayWithNullValue = {"tobias.stamann@holisticon.de", null};


        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((String[])null));
        MatcherAssert.assertThat("Expect to return true in case of null valued element", unit.validate(matchingArrayWithNullValue));


    }

}
