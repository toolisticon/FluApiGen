package io.toolisticon.fluapigen.processor;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link FluentApiProcessorCompilerMessages}.
 *
 *
 */
public class FluentApiProcessorMessagesTest {

    @Test
    public void test_enum() {

        MatcherAssert.assertThat(FluentApiProcessorCompilerMessages.ERROR_COULD_NOT_CREATE_CLASS.getCode(), Matchers.startsWith("FLUAPIGEN"));
        MatcherAssert.assertThat(FluentApiProcessorCompilerMessages.ERROR_COULD_NOT_CREATE_CLASS.getMessage(), Matchers.containsString("create class"));

    }


}
