package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.PassIn;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * Unit tests for {@link CustomFluentApiBackingBeanWrapperCode}
 */
public class CustomFluentApiBackingBeanWrapperCodeTest {

    CompileTestBuilder.UnitTestBuilder unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = CompileTestBuilder
                .unitTest();
    }

    @FluentApi("MyTestApi")
    static class NotAccessibleTestApi {

        @FluentApiBackingBean
        @PassIn
        private interface BackingBean {

        }


    }

    @Test
    public void validationTest_NoRoot() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(NotAccessibleTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiBackingBeanWrapperCode.validate(FluentApiBackingBeanWrapper.wrap(typeElement)));
                    }
                })
                .compilationShouldFail()
                .expectErrorMessage().thatContains(CoreMatcherValidationMessages.BY_MODIFIER.getCode())
                .executeTest();


    }

    @FluentApi("MyTestApi")
    static class PlacedOnClassTestApi {

        @FluentApiBackingBean
        @PassIn
        static class BackingBean {

        }

    }

    @Test
    public void validationTest_PlacedOnClass() {

        unitTestBuilder.<TypeElement>defineTestWithPassedInElement(PlacedOnClassTestApi.class, new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiBackingBeanWrapperCode.validate(FluentApiBackingBeanWrapper.wrap(typeElement)));
                    }
                })
                .compilationShouldFail()
                .expectErrorMessage().thatContains(CoreMatcherValidationMessages.IS_INTERFACE.getCode())
                .executeTest();


    }

}