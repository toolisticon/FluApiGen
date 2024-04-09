package io.toolisticon.fluapigen.integrationtest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.toolisticon.cute.GenericsTest;
import io.toolisticon.cute.InheritenceTestBuilder;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

public class GenericsTestTest {

    @Test
    public void testFiltering(){

        MatcherAssert.assertThat(InheritenceTestBuilder.valuesToFilter(Arrays.asList("abc",1,3,5,"def")).isInteger().close(), Matchers.contains(1,3,5));
        MatcherAssert.assertThat(InheritenceTestBuilder.valuesToFilter(Arrays.asList("abc",1,3,5,"def")).isString().close(), Matchers.contains("abc", "def"));

    }

}
