package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.wrapper.AnnotationMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.PassIn;
import io.toolisticon.cute.UnitTest;
import io.toolisticon.fluapigen.validation.api.FluentApiValidator;
import io.toolisticon.fluapigen.validation.api.Matches;
import io.toolisticon.fluapigen.validation.api.Validator;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.VariableElement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ValidatorTest {

    CompileTestBuilder.UnitTestBuilder unitTestBuilder;
    CompileTestBuilder.CompilationTestBuilder compilationTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = CompileTestBuilder
                .unitTest();

        compilationTestBuilder = CompileTestBuilder.compilationTest().addProcessors(FluentApiProcessor.class);
    }


    interface TestInterfaceWithMatchesValidator {
        TestInterfaceWithMatchesValidator doSomething(@PassIn @Matches("aaa.*") String parameter);
    }

    @Test
    public void testMatchesValidator() {
        unitTestBuilder.defineTestWithPassedInElement(TestInterfaceWithMatchesValidator.class, (UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), VariableElementWrapper.wrap(element).getAnnotationMirror(Matches.class).get());
                        MatcherAssert.assertThat(unit.validatorExpression(), Matchers.is("new io.toolisticon.fluapigen.validation.api.Matches.ValidatorImpl(\"aaa.*\")"));

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                }).compilationShouldSucceed()
                .executeTest();
    }

    @Test
    public void compilationTestValidator() {
        compilationTestBuilder.addSources("/testcases/TestcaseValidator.java")
                .compilationShouldSucceed()
                .executeTest();
    }


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @FluentApiValidator(value = AttributeValueStringRepresentationTest.MyValidator.class, attributeNamesToConstructorParameterMapping = {"longValue", "intValue", "floatValue", "doubleValue", "booleanValue", "enumValue", "stringValue", "arrayValue", "typeValue"})
    public @interface AttributeValueStringRepresentationTest {
        long longValue();

        int intValue();

        float floatValue();

        double doubleValue();

        boolean booleanValue();

        TestEnum enumValue();

        String stringValue();

        String[] arrayValue();

        Class<?> typeValue();

        class MyValidator implements Validator<String> {

            final long longValue;
            final int intValue;
            final float floatValue;
            final double doubleValue;

            final boolean booleanValue;

            final TestEnum enumValue;

            final String stringValue;

            final String[] arrayValue;

            final Class<?> typeValue;

            public MyValidator(long longValue, int intValue, float floatValue, double doubleValue, boolean booleanValue, TestEnum enumValue, String stringValue, String[] arrayValue, Class<?> typeValue) {
                this.longValue = longValue;
                this.intValue = intValue;
                this.floatValue = floatValue;
                this.doubleValue = doubleValue;
                this.booleanValue = booleanValue;
                this.enumValue = enumValue;
                this.stringValue = stringValue;
                this.arrayValue = arrayValue;
                this.typeValue = typeValue;
            }

            @Override
            public boolean validate(String obj) {
                return false;
            }
        }

    }


    public enum TestEnum {
        TEST;
    }

    public interface TestCase {


        void testCase(@PassIn @AttributeValueStringRepresentationTest(longValue = 1L, intValue = 2, floatValue = 3f, doubleValue = 4.0, booleanValue = true, enumValue = TestEnum.TEST, stringValue = "TEST", typeValue = String.class, arrayValue = {"ARRAY1", "ARRAY2"})
                      String parameter);

    }

    @Test
    public void test_getValidatorExpressionAttributeValueStringRepresentation() {
        unitTestBuilder.defineTestWithPassedInElement(TestCase.class, (UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        AnnotationMirrorWrapper annotationMirrorWrapper = AnnotationMirrorWrapper.get(element, AttributeValueStringRepresentationTest.class).get();

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), annotationMirrorWrapper);

                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("longValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("longValue").get()), Matchers.is("1L"));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("intValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("intValue").get()), Matchers.is("2"));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("floatValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("floatValue").get()), Matchers.is("3.0f"));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("doubleValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("doubleValue").get()), Matchers.is("4.0"));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("booleanValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("booleanValue").get()), Matchers.is("true"));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("enumValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("enumValue").get()), Matchers.is("io.toolisticon.fluapigen.processor.ValidatorTest.TestEnum.TEST"));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("stringValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("stringValue").get()), Matchers.is("\"TEST\""));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("typeValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("typeValue").get()), Matchers.is("java.lang.String.class"));
                        MatcherAssert.assertThat(unit.getValidatorExpressionAttributeValueStringRepresentation(annotationMirrorWrapper.getAttribute("arrayValue").get(), annotationMirrorWrapper.getAttributeTypeMirror("arrayValue").get()), Matchers.is("new java.lang.String[]{\"ARRAY1\", \"ARRAY2\"}"));


                    } finally {
                        ToolingProvider.clearTooling();
                    }

                }).compilationShouldSucceed()
                .executeTest();
    }

    @Test
    public void test_validate() {
        unitTestBuilder.defineTestWithPassedInElement(TestCase.class, (UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        AnnotationMirrorWrapper annotationMirrorWrapper = AnnotationMirrorWrapper.get(element, AttributeValueStringRepresentationTest.class).get();

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), annotationMirrorWrapper);

                        MatcherAssert.assertThat("Validation must be true", unit.validate());

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                }).compilationShouldSucceed()
                .executeTest();
    }


}
