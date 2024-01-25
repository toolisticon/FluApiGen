package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
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
import javax.lang.model.element.TypeElement;
import java.util.stream.Collectors;

/**
 * Unit tests for {@link CustomFluentApiWrapperCode}
 */
public class CustomFluentApiWrapperCodeTest {

    CuteApi.UnitTestRootInterface unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = Cute
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

        unitTestBuilder.when().passInElement().<TypeElement>fromClass(NoRootTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_NO_ROOT_INTERFACE.getCode())
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

        unitTestBuilder.when().passInElement().<TypeElement>fromClass(MultipleRootTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_MULTIPLE_ROOT_INTERFACES.getCode())
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

        unitTestBuilder.when().passInElement().<TypeElement>fromClass(EmptyStarterNameTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_CLASSNAME_MUST_NOT_BE_EMPTY.getCode())
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

        unitTestBuilder.when().passInElement().<TypeElement>fromClass(InvalidStarterNameTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_CLASSNAME_MUST_BE_VALID.getCode())
                .executeTest();


    }

    @Test
    public void validationTest_HappyPath() {

        unitTestBuilder.when().passInElement().<TypeElement>fromClass(ValidTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return true", CustomFluentApiWrapperCode.validate(FluentApiWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat().compilationSucceeds()
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
    public void getStateTest() {
        unitTestBuilder.when().passInElement().<TypeElement>fromClass(ValidTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        FluentApiState state = CustomFluentApiWrapperCode.getState(FluentApiWrapper.wrap(typeElement));

                        MatcherAssert.assertThat(state.getFluentApiInterfaceWrappers().stream().map(e -> e._annotatedElement().asType().toString()).collect(Collectors.toList()),
                                Matchers.containsInAnyOrder(
                                        ValidTestApi.FluentInterface1.class.getCanonicalName(),
                                        ValidTestApi.FluentInterface2.class.getCanonicalName())
                        );
                        MatcherAssert.assertThat(state.getFluentApiCommandWrappers().stream().map(e -> e._annotatedElement().asType().toString()).collect(Collectors.toList()),
                                Matchers.containsInAnyOrder(
                                        ValidTestApi.TestCommand1.class.getCanonicalName(),
                                        ValidTestApi.TestCommand2.class.getCanonicalName())
                        );
                        MatcherAssert.assertThat(state.getFluentApiBackingBeanWrappers().stream().map(e -> e._annotatedElement().asType().toString()).collect(Collectors.toList()),
                                Matchers.containsInAnyOrder(
                                        ValidTestApi.BackingBean1.class.getCanonicalName(),
                                        ValidTestApi.BackingBean2.class.getCanonicalName())
                        );
                    }
                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();

    }


}
