package io.toolisticon.fluapigen.validation.api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

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
    public void doValidationTest_multipleValues() {

        NotNull.ValidatorImpl unit = new NotNull.ValidatorImpl();

        final String[] matchingArray = {"tobias.stamann@holisticon.de", "tobias.stamann@holisticon.de"};
        final String[] nonMatchingArray = {"tobias.stamann@holisticon.de", null};


        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return false in case of null value", !unit.validate((String[]) null));


    }

}
