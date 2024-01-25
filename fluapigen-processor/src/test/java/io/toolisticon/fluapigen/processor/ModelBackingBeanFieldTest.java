package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
import io.toolisticon.cute.PassIn;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;

public class ModelBackingBeanFieldTest {

    CuteApi.UnitTestRootInterface unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = Cute
                .unitTest();
    }


    interface BackingBeanFieldIdTest {

        @FluentApiBackingBeanField
        @PassIn
        String myMethodName();

    }

    interface BackingBeanFieldIdWithExplicitIdTest {

        @FluentApiBackingBeanField(value = "myId")
        @PassIn
        String myMethodName();

    }

    @Test
    public void validationTest_PlacedOnClass() {

        unitTestBuilder.
                when().passInElement().<ExecutableElement>fromClass(BackingBeanFieldIdTest.class)
                .intoUnitTest(new APTKUnitTestProcessor<ExecutableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, ExecutableElement element) {

                        ModelBackingBeanField unit = new ModelBackingBeanField(ExecutableElementWrapper.wrap(element));


                        MatcherAssert.assertThat(unit.getFieldId(), Matchers.is("myMethodName"));


                    }
                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();


    }


    @Test
    public void validationTest_PlacedOnClass_withExplicitId() {

        unitTestBuilder.when().passInElement().<ExecutableElement>fromClass(BackingBeanFieldIdWithExplicitIdTest.class)
                .intoUnitTest(new APTKUnitTestProcessor<ExecutableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, ExecutableElement element) {

                        ModelBackingBeanField unit = new ModelBackingBeanField(ExecutableElementWrapper.wrap(element));
                        MatcherAssert.assertThat(unit.getFieldId(), Matchers.is("myId"));

                    }
                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();


    }


}
