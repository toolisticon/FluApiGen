package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.PassIn;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/**
 * Unit tests for {@link CustomFluentApiCommandWrapperCode}
 */
public class CustomFluentApiCommandWrapperCodeTest {

    CompileTestBuilder.UnitTestBuilder unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = CompileTestBuilder
                .unitTest();
    }


    @FluentApi("MyTestApi")
    static class HappyPathTestApi {
        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiInterface(BackingBean.class)
        @FluentApiRoot
        interface RootInterface {

        }

        @FluentApiCommand(RootInterface.class)
        @PassIn
        static class MyCommand {

            static void doSomethingBeautiful(BackingBean backingBean) {

            }

        }


    }

    @Test
    public void validationTest_HappyPath() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(HappyPathTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return true", CustomFluentApiCommandWrapperCode.validate(FluentApiCommandWrapper.wrap(typeElement)));
                    }
                })
                .compilationShouldSucceed()
                .executeTest();


    }

    @Test
    public void test_getCommandMethodName_onCommandClass() {
        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(HappyPathTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat(CustomFluentApiCommandWrapperCode.getCommandMethodName(FluentApiCommandWrapper.wrap(typeElement)), Matchers.is("doSomethingBeautiful"));
                    }
                })
                .compilationShouldSucceed()
                .executeTest();
    }


    @FluentApi("MyTestApi")
    static class OnFluentApiMethodTestApi {
        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiInterface(BackingBean.class)
        interface MyInterface {

            @FluentApiCommand(MyCommand.class)
            @PassIn
            void myCommandCall();
        }

        @FluentApiCommand(BackingBean.class)
        static class MyCommand {

            static void doSomethingBeautiful(BackingBean backingBean) {

            }

        }


    }

    @Test
    public void test_getCommandMethodName_onFluentInterfaceMethods() {
        unitTestBuilder.<ExecutableElement>defineTestWithPassedInElement(OnFluentApiMethodTestApi.class, new APTKUnitTestProcessor<ExecutableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, ExecutableElement typeElement) {

                        MatcherAssert.assertThat(CustomFluentApiCommandWrapperCode.getCommandMethodName(FluentApiCommandWrapper.wrap(typeElement)), Matchers.is("doSomethingBeautiful"));
                    }
                })
                .compilationShouldSucceed()
                .executeTest();
    }

    @FluentApi("MyTestApi")
    static class OnFluentApiMethodTestApi_InClass_error {
        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiInterface(BackingBean.class)
        private static class MyClass {

            @FluentApiCommand(MyCommand.class)
            @PassIn
            void myCommandCall() {

            }
        }

        @FluentApiCommand(BackingBean.class)
        static class MyCommand {

            static void doSomethingBeautiful(BackingBean backingBean) {

            }

        }


    }

    @Test
    public void test_validate_onMethodInClass() {
        unitTestBuilder.<ExecutableElement>defineTestWithPassedInElement(OnFluentApiMethodTestApi_InClass_error.class, new APTKUnitTestProcessor<ExecutableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, ExecutableElement element) {

                        FluentApiCommandWrapper unit = FluentApiCommandWrapper.wrap(element);
                        try {

                            ToolingProvider.setTooling(processingEnvironment);
                            unit.validate();

                        } finally {
                            ToolingProvider.clearTooling();
                        }
                    }
                })
                .compilationShouldFail()
                .expectErrorMessageThatContains(CoreMatcherValidationMessages.IS_INTERFACE.getCode())
                .executeTest();
    }


    @FluentApi("MyTestApi")
    static class MultipleMethodsInCommandClass {
        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiInterface(BackingBean.class)
        interface MyInterface {

            @FluentApiCommand(MyCommand.class)
            void myCommandCall();
        }

        @FluentApiCommand(BackingBean.class)
        @PassIn
        static class MyCommand {

            static void doSomethingBeautiful(BackingBean backingBean) {

            }

            static void secondMethod(BackingBean backingBean) {

            }

        }


    }

    @Test
    public void test_validate_twoMethodsInCommandClass() {
        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(MultipleMethodsInCommandClass.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        FluentApiCommandWrapper unit = FluentApiCommandWrapper.wrap(typeElement);
                        try {

                            ToolingProvider.setTooling(processingEnvironment);
                            unit.validate();

                        } finally {
                            ToolingProvider.clearTooling();
                        }

                    }
                })
                .compilationShouldFail()
                .expectErrorMessageThatContains(FluentApiProcessorCompilerMessages.ERROR_COMMAND_CLASS_MUST_DECLARE_EXACTLY_ONE_STATIC_METHOD.getCode())
                .executeTest();
    }


    @FluentApi("MyTestApi")
    static class NoMethodInCommandClass {
        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiInterface(BackingBean.class)
        interface MyInterface {

            @FluentApiCommand(MyCommand.class)
            void myCommandCall();
        }

        @FluentApiCommand(BackingBean.class)
        @PassIn
        static class MyCommand {

            static void doSomethingBeautiful(BackingBean backingBean) {

            }

            static void secondMethod(BackingBean backingBean) {

            }

        }


    }

    @Test
    public void test_validate_noMethodInCommandClass() {
        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(NoMethodInCommandClass.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        FluentApiCommandWrapper unit = FluentApiCommandWrapper.wrap(typeElement);
                        try {

                            ToolingProvider.setTooling(processingEnvironment);
                            unit.validate();

                        } finally {
                            ToolingProvider.clearTooling();
                        }

                    }
                })
                .compilationShouldFail()
                .expectErrorMessageThatContains(FluentApiProcessorCompilerMessages.ERROR_COMMAND_CLASS_MUST_DECLARE_EXACTLY_ONE_STATIC_METHOD.getCode())
                .executeTest();
    }

}