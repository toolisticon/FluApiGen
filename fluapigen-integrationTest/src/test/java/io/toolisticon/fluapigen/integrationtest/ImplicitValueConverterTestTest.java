package io.toolisticon.fluapigen.integrationtest;

import io.toolisticon.cute.ImplicitValueConverterTest;
import io.toolisticon.cute.ImplicitValueConverterTestBuilder;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class ImplicitValueConverterTestTest {

    @Test
    public void test() {

        ImplicitValueConverterTest.TestBackingBean testBackingBean = ImplicitValueConverterTestBuilder.setSingleValue()
                .setCollectionValue()
                .addCollectionValue()
                .setArrayValue()
                .myCommand();

        MatcherAssert.assertThat(testBackingBean.collectionValue().stream().map(e -> e.toString()).collect(Collectors.toList()), Matchers.contains("ONE", "TWO", "THREE", "A", "B", "C"));
        MatcherAssert.assertThat(Arrays.asList(testBackingBean.arrayValue()).stream().map(e -> e.toString()).collect(Collectors.toList()), Matchers.contains( "A", "B", "C"));
        MatcherAssert.assertThat(testBackingBean.singleValue().toString(), Matchers.is( "SINGLE"));



    }

}
