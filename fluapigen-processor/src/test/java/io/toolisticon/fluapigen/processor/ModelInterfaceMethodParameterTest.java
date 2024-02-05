package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.cute.APTKUnitTestProcessor;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.PassIn;
import io.toolisticon.fluapigen.validation.api.Matches;
import io.toolisticon.fluapigen.validation.api.NotNull;
import io.toolisticon.fluapigen.validation.api.Nullable;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.VariableElement;
import java.util.stream.Collectors;

/**
 * Unit test for {@link ModelInterfaceMethodParameter}.
 */
public class ModelInterfaceMethodParameterTest {

    interface TestClassWithoutValidators {

        public void testMethod(@PassIn String parameter);

    }


    @Test
    public void test_getValidators_withoutValidatorsPresent() {
        Cute.unitTest().when().passInElement().<VariableElement>fromClass(TestClassWithoutValidators.class)
                .intoUnitTest(new APTKUnitTestProcessor<VariableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, VariableElement variableElement) {

                        ModelInterfaceMethodParameter unit = new ModelInterfaceMethodParameter(VariableElementWrapper.wrap(variableElement), null, null);

                        MatcherAssert.assertThat("Expect to find no validators", !unit.hasValidators());

                    }
                }).executeTest();
    }

    @NotNull
    interface TestClassWithValidators {

        public void testMethod(@PassIn @Matches(".*[@].*") String parameter);

    }

    @Test
    public void test_getValidators_withValidatorsPresent() {
        Cute.unitTest().when().passInElement().<VariableElement>fromClass(TestClassWithValidators.class)
                .intoUnitTest(new APTKUnitTestProcessor<VariableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, VariableElement variableElement) {

                        ModelInterfaceMethodParameter unit = new ModelInterfaceMethodParameter(VariableElementWrapper.wrap(variableElement), null, null);

                        MatcherAssert.assertThat("Expect to find validators", unit.hasValidators());
                        MatcherAssert.assertThat("Expect to find 2 validators", unit.getValidators().size() == 2);
                        MatcherAssert.assertThat(unit.getValidators().stream().map(e -> e.getAnnotationMirrorWrapper().asTypeMirror().getQualifiedName()).collect(Collectors.toList()), Matchers.containsInAnyOrder(NotNull.class.getCanonicalName(), Matches.class.getCanonicalName()));


                    }
                }).executeTest();
    }

    @NotNull
    interface TestClassWithOverruledValidators {

        void testMethod(@PassIn @Nullable @Matches(".*[@].*") String parameter);

    }

    @Test
    public void test_getValidators_withOverruledValidatorsPresent() {
        Cute.unitTest().when().passInElement().<VariableElement>fromClass(TestClassWithOverruledValidators.class)
                .intoUnitTest(new APTKUnitTestProcessor<VariableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, VariableElement variableElement) {

                        ModelInterfaceMethodParameter unit = new ModelInterfaceMethodParameter(VariableElementWrapper.wrap(variableElement), null, null);

                        MatcherAssert.assertThat("Expect to find validators", unit.hasValidators());
                        MatcherAssert.assertThat("Expect to find 2 validators", unit.getValidators().size() == 2);
                        MatcherAssert.assertThat(unit.getValidators().stream().map(e -> e.getAnnotationMirrorWrapper().asTypeMirror().getQualifiedName()).collect(Collectors.toList()), Matchers.containsInAnyOrder(Nullable.class.getCanonicalName(), Matches.class.getCanonicalName()));


                    }
                }).executeTest();
    }

    @Nullable
    interface TestClassWithOverruledValidators2 {

        void testMethod(@PassIn @NotNull @Matches(".*[@].*") String parameter);

    }

    @Test
    public void test_getValidators_withOverruledValidatorsPresent2() {
        Cute.unitTest().when().passInElement().<VariableElement>fromClass(TestClassWithOverruledValidators2.class)
                .intoUnitTest(new APTKUnitTestProcessor<VariableElement>() {
                    @Override
                    public void aptkUnitTest(ProcessingEnvironment processingEnvironment, VariableElement variableElement) {

                        ModelInterfaceMethodParameter unit = new ModelInterfaceMethodParameter(VariableElementWrapper.wrap(variableElement), null, null);

                        MatcherAssert.assertThat("Expect to find validators", unit.hasValidators());
                        MatcherAssert.assertThat("Expect to find 2 validators", unit.getValidators().size() == 2);
                        MatcherAssert.assertThat(unit.getValidators().stream().map(e -> e.getAnnotationMirrorWrapper().asTypeMirror().getQualifiedName()).collect(Collectors.toList()), Matchers.containsInAnyOrder(NotNull.class.getCanonicalName(), Matches.class.getCanonicalName()));


                    }
                }).executeTest();
    }

}
