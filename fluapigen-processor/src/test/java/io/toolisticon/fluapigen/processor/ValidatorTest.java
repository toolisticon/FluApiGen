package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.wrapper.AnnotationMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
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

    CuteApi.UnitTestRootInterface unitTestBuilder;
    CuteApi.BlackBoxTestSourceFilesInterface compilationTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        unitTestBuilder = Cute
                .unitTest();

        compilationTestBuilder = Cute.blackBoxTest().given().processors(FluentApiProcessor.class);
    }


    interface TestInterfaceWithMatchesValidator {
        TestInterfaceWithMatchesValidator doSomething(@PassIn @Matches("aaa.*") String parameter);
    }

    @Test
    public void testMatchesValidator() {
        unitTestBuilder.when().passInElement().<VariableElement>fromClass(TestInterfaceWithMatchesValidator.class)
                .intoUnitTest((UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), VariableElementWrapper.wrap(element).getAnnotationMirror(Matches.class).get());
                        MatcherAssert.assertThat(unit.validatorExpression(), Matchers.is("new io.toolisticon.fluapigen.validation.api.Matches.ValidatorImpl(\"aaa.*\")"));

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();
    }

    @Test
    public void compilationTestValidator() {
        compilationTestBuilder.andSourceFiles("/testcases/TestcaseValidator.java")
                .whenCompiled().thenExpectThat()
                .compilationSucceeds()
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
        unitTestBuilder.when().passInElement().<VariableElement>fromClass(TestCase.class)
                .intoUnitTest((UnitTest<VariableElement>) (processingEnvironment, element) -> {

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

                }).thenExpectThat().compilationSucceeds()
                .executeTest();
    }

    @Test
    public void test_validate() {
        unitTestBuilder.when().passInElement().<VariableElement>fromClass(TestCase.class)
                .intoUnitTest((UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        AnnotationMirrorWrapper annotationMirrorWrapper = AnnotationMirrorWrapper.get(element, AttributeValueStringRepresentationTest.class).get();

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), annotationMirrorWrapper);

                        MatcherAssert.assertThat("Validation must be true", unit.validate());

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                })
                .thenExpectThat().compilationSucceeds()
                .executeTest();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @FluentApiValidator(value = WrongAttributeNameToConstructorParameterMapping.MyValidator.class, attributeNamesToConstructorParameterMapping = {"sadas"})
    @interface WrongAttributeNameToConstructorParameterMapping {

        String attr();

        public class MyValidator implements Validator<String> {

            final String attr;

            public MyValidator(String attr) {
                this.attr = attr;
            }

            @Override
            public boolean validate(String obj) {

                return false;
            }
        }

    }


    interface WrongAttributeNameToConstructorParameterMappingTest {

        void method(@PassIn @WrongAttributeNameToConstructorParameterMapping(attr = "abc") String parameter);

    }

    @Test
    public void test_validate_invalidValidatorWrong() {
        unitTestBuilder.
                when().passInElement().<VariableElement>fromClass(WrongAttributeNameToConstructorParameterMappingTest.class)
                .intoUnitTest((UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        AnnotationMirrorWrapper annotationMirrorWrapper = AnnotationMirrorWrapper.get(element, WrongAttributeNameToConstructorParameterMapping.class).get();

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), annotationMirrorWrapper);

                        MatcherAssert.assertThat("Validation must be false", !unit.validate());

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_BROKEN_VALIDATOR_ATTRIBUTE_NAME_MISMATCH.getCode())
                .executeTest();
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @FluentApiValidator(value = MissingNoargConstructor.MyValidator.class)
    @interface MissingNoargConstructor {

        class MyValidator implements Validator<String> {

            public MyValidator(String attr) {

            }

            @Override
            public boolean validate(String obj) {

                return false;
            }
        }

    }


    interface MissingNoargConstructorTest {

        void method(@PassIn @MissingNoargConstructor String parameter);

    }

    @Test
    public void test_validate_missingNoargConstructor() {
        unitTestBuilder.when().passInElement().<VariableElement>fromClass(MissingNoargConstructorTest.class)
                .intoUnitTest((UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        AnnotationMirrorWrapper annotationMirrorWrapper = AnnotationMirrorWrapper.get(element, MissingNoargConstructor.class).get();

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), annotationMirrorWrapper);

                        MatcherAssert.assertThat("Validation must be false", !unit.validate());

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_BROKEN_VALIDATOR_MISSING_NOARG_CONSTRUCTOR.getCode())
                .executeTest();
    }


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @FluentApiValidator(value = NoMatchingConstructor.MyValidator.class, attributeNamesToConstructorParameterMapping = {"attr"})
    @interface NoMatchingConstructor {

        String attr();

        class MyValidator implements Validator<String> {

            public MyValidator(Class<?> attr) {

            }

            @Override
            public boolean validate(String obj) {

                return false;
            }
        }

    }


    interface NonMatchingConstructorTest {

        void method(@PassIn @NoMatchingConstructor(attr = "TEST") String parameter);

    }

    @Test
    public void test_validate_noMatchingConstructor() {
        unitTestBuilder.when().passInElement().<VariableElement>fromClass(NonMatchingConstructorTest.class)
                .intoUnitTest((UnitTest<VariableElement>) (processingEnvironment, element) -> {

                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        AnnotationMirrorWrapper annotationMirrorWrapper = AnnotationMirrorWrapper.get(element, NoMatchingConstructor.class).get();

                        ModelValidator unit = new ModelValidator(VariableElementWrapper.wrap(element), annotationMirrorWrapper);

                        MatcherAssert.assertThat("Validation must be false", !unit.validate());

                    } finally {
                        ToolingProvider.clearTooling();
                    }

                })
                .thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_BROKEN_VALIDATOR_CONSTRUCTOR_PARAMETER_MAPPING.getCode())
                .executeTest();
    }

}
