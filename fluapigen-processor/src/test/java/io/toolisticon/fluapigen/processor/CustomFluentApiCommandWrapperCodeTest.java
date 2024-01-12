package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
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

}