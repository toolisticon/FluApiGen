package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.JavaFileObjectUtils;
import org.junit.Before;
import org.junit.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;


/**
 * Tests of {@link io.toolisticon.fluapigen.api.FluentApi}.
 *
 * TODO: replace the example testcases with your own testcases
 */

public class FluentApiProcessorTest {


    CompileTestBuilder.CompilationTestBuilder compileTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        compileTestBuilder = CompileTestBuilder
                .compilationTest()
                .addProcessors(FluentApiProcessor.class);
    }

/*-
    @Test
    public void test_valid_usage() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/TestcaseValidUsage.java"))
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.Xyz")
                .executeTest();
    }


    @Test
    public void test_valid_usage2() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/ExampleFluentApi.java"))
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.ExampleFluentApiStarter")
                .executeTest();
    }
*/
    @Test
    public void test_valid_usage3() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/CuteFluentApi.java"))
                //.compilationShouldFail()
                .compilationShouldSucceed()
                //.expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.ExampleFluentApiStarter")
                .executeTest();
    }


}