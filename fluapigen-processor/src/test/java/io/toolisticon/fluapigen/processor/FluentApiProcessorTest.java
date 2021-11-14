package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.Before;
import org.junit.Test;

public class FluentApiProcessorTest {

    CompileTestBuilder.CompilationTestBuilder compileTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);


    }


    @Test
    public void test_backingBeanCreation() {
        CompileTestBuilder.compilationTest()
                .addProcessors(FluentApiProcessor.class)
                .addSources("testcases.backingbean/TestFluentApi.java")
                //.expectThatJavaFileObjectExists(StandardLocation.SOURCE_OUTPUT, "io.toolisticon.fluapigen.test.TestBackingBeanImpl", JavaFileObject.Kind.SOURCE)
                //.compilationShouldFail()
                .compilationShouldSucceed()
                .executeTest();
    }
}
