package io.toolisticon.fluapigen.validation.api;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

import java.lang.reflect.Modifier;

/**
 * Unit test for {@link MaxLength}.
 */
public class HasNoArgConstructorTest {

    public static class TestHasDefaultNoarg {

    }

    public static class TestHasExplicitNoarg {
        TestHasExplicitNoarg() {

        }
    }

    public static class TestHasNoNoarg {
        TestHasNoNoarg(String arg) {

        }
    }

    public static abstract class TestHasNoargButIsAbstract {
        TestHasNoargButIsAbstract(String arg) {

        }
    }


    @Test
    public void doValidationTest_singleValue() {

        int[] modifiers = {};

        HasNoArgConstructor.ValidatorImpl unit = new HasNoArgConstructor.ValidatorImpl(modifiers);

        MatcherAssert.assertThat("Expect to match", unit.validate(TestHasDefaultNoarg.class));
        MatcherAssert.assertThat("Expect to match", unit.validate(TestHasExplicitNoarg.class));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(TestHasNoNoarg.class));
        MatcherAssert.assertThat("Expect to return true in case of null ", unit.validate((Class<?>) null));


        int[] expectedModifiers = {Modifier.PUBLIC};
        HasNoArgConstructor.ValidatorImpl unitWithModifiers = new HasNoArgConstructor.ValidatorImpl(expectedModifiers);
        MatcherAssert.assertThat("Expect to match", unitWithModifiers.validate(TestHasDefaultNoarg.class));


    }

    @Test
    public void doValidationTest_singleValue_withModifier() {

        int[] expectedModifiers = {Modifier.PUBLIC};
        HasNoArgConstructor.ValidatorImpl unitWithModifiers = new HasNoArgConstructor.ValidatorImpl(expectedModifiers);
        MatcherAssert.assertThat("Expect to match", unitWithModifiers.validate(TestHasDefaultNoarg.class));

        int[] expectedModifiers_nonMatching = {Modifier.PROTECTED};
        unitWithModifiers = new HasNoArgConstructor.ValidatorImpl(expectedModifiers_nonMatching);
        MatcherAssert.assertThat("Expect not to match", !unitWithModifiers.validate(TestHasDefaultNoarg.class));

    }


    @Test
    public void doValidationTest_multipleValues() {
        int[] modifiers = {};

        HasNoArgConstructor.ValidatorImpl unit = new HasNoArgConstructor.ValidatorImpl(modifiers);

        final Class<?>[] matchingArray = {TestHasDefaultNoarg.class, TestHasExplicitNoarg.class};
        final Class<?>[] nonMatchingArray = {TestHasDefaultNoarg.class, TestHasNoNoarg.class, TestHasExplicitNoarg.class};
        final Class<?>[] matchingArrayWithNullValue = {TestHasDefaultNoarg.class, null};

        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((Class<?>[]) null));
        MatcherAssert.assertThat("Expect to return true in case of null element value", unit.validate(matchingArrayWithNullValue));


    }

    @Test
    public void doValidationTest_abstractClass() {
        int[] modifiers = {Modifier.PUBLIC};

        HasNoArgConstructor.ValidatorImpl unit = new HasNoArgConstructor.ValidatorImpl(modifiers);

        final Class<?>[] matchingArray = {TestHasDefaultNoarg.class, TestHasExplicitNoarg.class};
        final Class<?>[] nonMatchingArray = {TestHasDefaultNoarg.class, TestHasNoargButIsAbstract.class, TestHasExplicitNoarg.class};
        final Class<?>[] matchingArrayWithNullValue = {TestHasDefaultNoarg.class, null};

        MatcherAssert.assertThat("Expect to match", unit.validate(matchingArray));
        MatcherAssert.assertThat("Expect not to match", !unit.validate(nonMatchingArray));
        MatcherAssert.assertThat("Expect to return true in case of null value", unit.validate((Class<?>[]) null));
        MatcherAssert.assertThat("Expect to return true in case of null element value", unit.validate(matchingArrayWithNullValue));


    }

}
