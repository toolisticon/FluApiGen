package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
import io.toolisticon.cute.PassIn;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * Unit tests for {@link CustomFluentApiBackingBeanWrapperCode}
 */
public class CustomFluentApiBackingBeanWrapperCodeTest {

    CuteApi.UnitTestRootInterface unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = Cute.unitTest();
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

        unitTestBuilder.when()
                .passInElement().<TypeElement>fromClass(NotAccessibleTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiBackingBeanWrapperCode.validate(FluentApiBackingBeanWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(CoreMatcherValidationMessages.BY_MODIFIER.getCode())
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

        unitTestBuilder.when()
                .passInElement().<TypeElement>fromClass(PlacedOnClassTestApi.class)
                .intoUnitTest(new APTKUnitTestProcessor<TypeElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, TypeElement typeElement) {

                        MatcherAssert.assertThat("should return false", !CustomFluentApiBackingBeanWrapperCode.validate(FluentApiBackingBeanWrapper.wrap(typeElement)));
                    }
                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(CoreMatcherValidationMessages.IS_INTERFACE.getCode())
                .executeTest();

    }

}