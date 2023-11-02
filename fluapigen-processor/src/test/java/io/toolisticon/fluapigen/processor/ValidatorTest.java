package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.PassIn;
import io.toolisticon.cute.UnitTest;
import io.toolisticon.fluapigen.api.validation.Matches;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.VariableElement;

public class ValidatorTest {

    CompileTestBuilder.UnitTestBuilder unitTestBuilder;
    CompileTestBuilder.CompilationTestBuilder compilationTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = CompileTestBuilder
                .unitTest();

        compilationTestBuilder = CompileTestBuilder.compilationTest().addProcessors(FluentApiProcessor.class);
    }


    interface TestInterfaceWithMatchesValidator {
        TestInterfaceWithMatchesValidator doSomething(@PassIn @Matches("aaa.*") String parameter);
    }

    @Test
    public void testMatchesValidator() {
        unitTestBuilder.defineTestWithPassedInElement(TestInterfaceWithMatchesValidator.class, (UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element).getAnnotationMirror(Matches.class).get());
                        MatcherAssert.assertThat(unit.validatorExpression(), Matchers.is("new io.toolisticon.fluapigen.api.validation.Matches.ValidatorImpl(\"aaa.*\")"));

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                }).compilationShouldSucceed()
                .executeTest();
    }

    @Test
    public void compilationTestValidator() {
        compilationTestBuilder.addSources("/testcases/TestcaseValidator.java")
                .compilationShouldSucceed()
                .executeTest();
    }


}
