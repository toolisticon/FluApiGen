package io.toolisticon.fluapigen.processor;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link FluentApiProcessorMessages2}.
 *
 * TODO: replace the example testcases with your own testcases
 *
 */
public class FluentApiProcessorMessages2Test {

    @Test
    public void test_enum() {

        MatcherAssert.assertThat(FluentApiProcessorMessages2.ERROR_COULD_NOT_CREATE_CLASS.getCode(), Matchers.startsWith("FluentApi"));
        MatcherAssert.assertThat(FluentApiProcessorMessages2.ERROR_COULD_NOT_CREATE_CLASS.getMessage(), Matchers.containsString("create class"));

    }


}
