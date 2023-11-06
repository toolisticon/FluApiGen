package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.PassIn;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

public class ModelBackingBeanFieldTest {

    CompileTestBuilder.UnitTestBuilder unitTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = CompileTestBuilder
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

        unitTestBuilder.<ExecutableElement>defineTestWithPassedInElement(BackingBeanFieldIdTest.class, new APTKUnitTestProcessor<ExecutableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, ExecutableElement element) {

                        ModelBackingBeanField unit = new ModelBackingBeanField(ExecutableElementWrapper.wrap(element));


                        MatcherAssert.assertThat(unit.getFieldId(), Matchers.is("myMethodName"));


                    }
                })
                .compilationShouldSucceed()
                .executeTest();


    }


    @Test
    public void validationTest_PlacedOnClass_withExplicitId() {

        unitTestBuilder.<ExecutableElement>defineTestWithPassedInElement(BackingBeanFieldIdWithExplicitIdTest.class, new APTKUnitTestProcessor<ExecutableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, ExecutableElement element) {

                        ModelBackingBeanField unit = new ModelBackingBeanField(ExecutableElementWrapper.wrap(element));
                        MatcherAssert.assertThat(unit.getFieldId(), Matchers.is("myId"));

                    }
                })
                .compilationShouldSucceed()
                .executeTest();


    }


}
