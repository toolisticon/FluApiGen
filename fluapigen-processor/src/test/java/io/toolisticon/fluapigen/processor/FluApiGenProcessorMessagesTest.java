package io.toolisticon.fluapigen.processor;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit test for {@link FluApiGenProcessorMessages}.
 * <p>
 * TODO: replace the example testcases with your own testcases
 */
public class FluApiGenProcessorMessagesTest {

    @Test
    public void test_enum() {

        MatcherAssert.assertThat(FluApiGenProcessorMessages.ERROR_INTERFACE_MUST_BE_BOUND_WITH_ONE_BACKING_BEAN.getCode(), Matchers.startsWith("FluApiGen"));

    }


}
