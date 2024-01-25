package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
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

    CuteApi.UnitTestRootInterface unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = Cute
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

        unitTestBuilder.when().passInElement().<TypeElement>fromClass(HappyPathTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {
                        MatcherAssert.assertThat("should return true", CustomFluentApiCommandWrapperCode.validate(FluentApiCommandWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat()
                .compilationSucceeds()
                .executeTest();


    }

    @Test
    public void test_getCommandMethodName_onCommandClass() {
        unitTestBuilder.when().passInElement().<TypeElement>fromClass(HappyPathTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat(CustomFluentApiCommandWrapperCode.getCommandMethodName(FluentApiCommandWrapper.wrap(typeElement)), Matchers.is("doSomethingBeautiful"));
                    }
                })
                .thenExpectThat().compilationSucceeds()
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
        unitTestBuilder.when()
                .passInElement().<ExecutableElement>fromClass(OnFluentApiMethodTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<ExecutableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, ExecutableElement typeElement) {

                        MatcherAssert.assertThat(CustomFluentApiCommandWrapperCode.getCommandMethodName(FluentApiCommandWrapper.wrap(typeElement)), Matchers.is("doSomethingBeautiful"));
                    }
                })
                .thenExpectThat()
                .compilationSucceeds()
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
        unitTestBuilder.when().passInElement().<ExecutableElement>fromClass(OnFluentApiMethodTestApi_InClass_error.class)
                .intoUnitTest(new APTKUnitTestProcessor<ExecutableElement>() {
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
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(CoreMatcherValidationMessages.IS_INTERFACE.getCode())
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
        unitTestBuilder.when().passInElement().<TypeElement>fromClass(MultipleMethodsInCommandClass.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
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
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_COMMAND_CLASS_MUST_DECLARE_EXACTLY_ONE_STATIC_METHOD.getCode())
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
        unitTestBuilder.when().passInElement().<TypeElement>fromClass(NoMethodInCommandClass.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
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
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_COMMAND_CLASS_MUST_DECLARE_EXACTLY_ONE_STATIC_METHOD.getCode())
                .executeTest();
    }

}