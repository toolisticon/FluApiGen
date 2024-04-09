package io.toolisticon.cute;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GenericsTestTest {

    @Test
    public void testAccessToBackingBean() {

        List<?> list = Arrays.asList("ABC",1);

        GenericsTest.MyRootBackingBean bb =((InheritenceTestBuilder.MyFilterInterfaceImpl)(InheritenceTestBuilder.valuesToFilter(list))).backingBean;
        MatcherAssert.assertThat(bb.values(), Matchers.containsInAnyOrder("ABC",1));

    }
}
