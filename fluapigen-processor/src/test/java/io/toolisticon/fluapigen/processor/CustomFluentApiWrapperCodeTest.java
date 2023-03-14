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
import javax.lang.model.element.TypeElement;
import java.util.stream.Collectors;

/**
 * Unit tests for {@link CustomFluentApiWrapperCode}
 */
public class CustomFluentApiWrapperCodeTest {

    CompileTestBuilder.UnitTestBuilder unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = CompileTestBuilder
                .unitTest();
    }

    @FluentApi("MyTestApi")
    @PassIn
    static class NoRootTestApi {

        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiInterface(BackingBean.class)
        interface FluentInterface {

        }

    }

    @Test
    public void validationTest_NoRoot() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(NoRootTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
            @Override
            public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                MatcherAssert.assertThat("should return false",!CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
            }
        })
        .compilationShouldFail()
        .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_NO_ROOT_INTERFACE.getCode())
        .executeTest();


    }

    @FluentApi("MyTestApi")
    @PassIn
    static class MultipleRootTestApi {

        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiRoot
        @FluentApiInterface(BackingBean.class)
        interface FluentInterface1 {

        }

        @FluentApiRoot
        @FluentApiInterface(BackingBean.class)
        interface FluentInterface2 {

        }

    }

    @Test
    public void validationTest_MultipleRoot() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(MultipleRootTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false",!CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_MULTIPLE_ROOT_INTERFACES.getCode())
                .executeTest();


    }

    @FluentApi("")
    @PassIn
    static class EmptyStarterNameTestApi {

        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiRoot
        @FluentApiInterface(BackingBean.class)
        interface FluentInterface1 {

        }


        @FluentApiInterface(BackingBean.class)
        interface FluentInterface2 {

        }

    }

    @Test
    public void validationTest_EmptyStarterName() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(EmptyStarterNameTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false",!CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_CLASSNAME_MUST_NOT_BE_EMPTY.getCode())
                .executeTest();


    }

    @FluentApi("3!dad")
    @PassIn
    static class InvalidStarterNameTestApi {

        @FluentApiBackingBean
        interface BackingBean {

        }

        @FluentApiRoot
        @FluentApiInterface(BackingBean.class)
        interface FluentInterface1 {

        }


        @FluentApiInterface(BackingBean.class)
        interface FluentInterface2 {

        }

    }

    @Test
    public void validationTest_InvalidStarterName() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(InvalidStarterNameTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false",!CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_CLASSNAME_MUST_BE_VALID.getCode())
                .executeTest();


    }

    @Test
    public void validationTest_HappyPath() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(ValidTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return true",CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .compilationShouldSucceed()
                .executeTest();

    }

    @FluentApi("TestApi")
    @PassIn
    static class ValidTestApi {

        @FluentApiBackingBean
        interface BackingBean1 {

        }

        @FluentApiBackingBean
        interface BackingBean2 {

        }

        @FluentApiRoot
        @FluentApiInterface(BackingBean1.class)
        interface FluentInterface1 {

        }


        @FluentApiInterface(BackingBean2.class)
        interface FluentInterface2 {

        }

        @FluentApiCommand
        static class TestCommand1 {

            static void myCommand(BackingBean1 bb) {

            }

        }

        @FluentApiCommand
        static class TestCommand2 {

            static void myCommand(BackingBean1 bb) {

            }

        }

    }

    @Test
    public void getStateTest () {
        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(ValidTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        FluentApiState state = CustomFluentApiWrapperCode.getState(FluentApiWrapper.wrap(typeElement));

                        MatcherAssert.assertThat(state.getFluentApiInterfaceWrappers().stream().map(e-> e._annotatedElement().asType().toString()).collect(Collectors.toList()),
                                Matchers.containsInAnyOrder(
                                        ValidTestApi.FluentInterface1.class.getCanonicalName(),
                                        ValidTestApi.FluentInterface2.class.getCanonicalName())
                        );
                        MatcherAssert.assertThat(state.getFluentApiCommandWrappers().stream().map(e-> e._annotatedElement().asType().toString()).collect(Collectors.toList()),
                                Matchers.containsInAnyOrder(
                                        ValidTestApi.TestCommand1.class.getCanonicalName(),
                                        ValidTestApi.TestCommand2.class.getCanonicalName())
                        );
                        MatcherAssert.assertThat(state.getFluentApiBackingBeanWrappers().stream().map(e-> e._annotatedElement().asType().toString()).collect(Collectors.toList()),
                                Matchers.containsInAnyOrder(
                                        ValidTestApi.BackingBean1.class.getCanonicalName(),
                                        ValidTestApi.BackingBean2.class.getCanonicalName())
                        );
                    }
                })
                .compilationShouldSucceed()
                .executeTest();

    }


}
