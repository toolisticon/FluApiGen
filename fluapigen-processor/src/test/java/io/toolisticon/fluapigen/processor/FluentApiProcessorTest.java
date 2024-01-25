package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.Cute;
import io.toolisticon.cute.CuteApi;
import io.toolisticon.cute.JavaFileObjectUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests of {@link io.toolisticon.fluapigen.api.FluentApi}.
 * <p>
 * TODO: replace the example testcases with your own testcases
 */

public class FluentApiProcessorTest {


    CuteApi.BlackBoxTestSourceFilesInterface compileTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        compileTestBuilder = Cute
                .blackBoxTest()
                .given().processors(FluentApiProcessor.class);
    }


    @Test
    public void test_valid_usage() {

        compileTestBuilder
                .andSourceFiles("testcases/TestcaseValidUsage.java")
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .andThat().generatedSourceFile("io.toolisticon.fluapigen.processor.tests.Xyz").exists()
                .executeTest();
    }

    @Test
    public void test_valid_usage_with_inline_bb_mapping() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/TestcaseValidUsageWithInlineBackingBeanMapping.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .andThat().generatedSourceFile("io.toolisticon.fluapigen.processor.tests.Xyz").exists()
                .executeTest();
    }


/*-
    @Test
    public void test_valid_usage2() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/ExampleFluentApi.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .andThat().generatedSourceFile("io.toolisticon.fluapigen.processor.tests.ExampleFluentApiStarter").exists()
                .executeTest();
    }
*/

    @Test
    public void test_valid_usage3() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/CuteFluentApi.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                //.andThat().generatedSourceFile("io.toolisticon.fluapigen.processor.tests.ExampleFluentApiStarter").exists()
                .executeTest();
    }

    @Test
    public void test_valid_usage4() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest.java"))
                //.whenCompiled().thenExpectThat().compilationFails()
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .andThat().generatedSourceFile("io.toolisticon.fluapigen.testcases.IntegrationTestStarter").exists()
                .executeTest();
    }

    @Test
    public void test_valid_usage_withDefaultMethods() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/TestcaseValidUsageWithDefaultMethods.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .andThat().generatedSourceFile("io.toolisticon.fluapigen.processor.tests.Xyz").exists()
                .executeTest();
    }

    @Test
    public void test_invalid_bbFieldNameMapping() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidBBFieldMapping.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_CANNOT_FIND_BACKING_BEAN_FIELD.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_ReturnTypeInFluentInterface() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidReturnTypeInFluentInterface.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_RETURN_TYPE_MUST_BE_FLUENT_INTERFACE.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_MappingAtParentTraversal() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidMappingAtParentTraversal.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_CANNOT_FIND_BACKING_BEAN_FIELD.getCode())
                .executeTest();

    }


    @Test
    public void test_invalid_MissingMappingAnnotationOnParentTraversal() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_MissingMappingAnnotationOnParentTraversal.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT_ADD_TO_PARENT_TRAVERSALS.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_ParameterAtCommand() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidParameterAtCommand.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_PARAMETER_OF_COMMAND_METHOD_MUST_BE_INTERFACE_ANNOTATED_AS_BACKING_BEAN.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_NoParameterAtCommand() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_NoParameterAtCommand.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(CoreMatcherValidationMessages.BY_NUMBER_OF_PARAMETERS.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_MultipleRootInterfaces() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidMultipleRootInterfaces.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_MULTIPLE_ROOT_INTERFACES.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_ImplicitValue_Int() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidImplicitValue_Int.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE.getCode())
                .executeTest();

    }

    @Test
    public void test_valid_MissingBBFieldAnnotation() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_MissingBBFieldAnnotation.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .executeTest();

    }

    @Test
    public void test_invalid_NonUniqueBBFieldId() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_NonUniqueBBFieldId.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_UNIQUE_IN_BB.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_NonUniqueBBFieldId_ImplicitlySet() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_NonUniqueBBFieldId_ImplicitlySet.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_UNIQUE_IN_BB.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_EmptyBBFieldId() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_EmptyBBFieldId.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_BE_EMPTY.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_MissingMethodParameterBBMapping() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_MissingMethodParameterBBMapping.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_UnsupportedImplicitValueType() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_UnsupportedImplicitValueType.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_InvalidImplicitValue_Enum() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidImplicitValue_Enum.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_InvalidNumberOfParentMappingsAtCommand() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/TestcaseInvalidNumberOfParentMappingsAtCommand.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().contains(FluentApiProcessorCompilerMessages.ERROR_INVALID_NUMBER_OF_PARENT_MAPPINGS_TO_REACH_ROOT_BB.getCode())
                .executeTest();


    }

    @Test
    public void test_valid_ImplicitValueConverter() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_ImplicitValueConverterTest.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .executeTest();


    }

    @Test
    public void test_invalid_ImplicitValueConverter_invalidTargetType() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLine(55).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLine(58).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLine(61).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLine(64).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_ImplicitValueConverter_invalidSourceType() {

        compileTestBuilder
                .andSourceFiles("testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java")
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLine(55).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLine(58).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLine(61).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .andThat().compilerMessage().ofKindError().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLine(64).contains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .executeTest();


    }

    @Test
    public void test_valid_BackingBeanMappingConverter() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongSingleValueSource() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongSourceSingleValue.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atLine(62).contains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongArrayValueSource() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongSourceArrayValue.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atLine(64).contains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongCollectionValueSource() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongSourceCollectionValue.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atLine(66).contains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongSingleValueTarget() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongTargetSingleValue.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atLine(62).contains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongArrayValueTarget() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongTargetArrayValue.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atLine(64).contains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongCollectionValueTarget() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongTargetCollectionValue.java"))
                .whenCompiled().thenExpectThat().compilationFails()
                .andThat().compilerMessage().ofKindError().atLine(66).contains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .executeTest();


    }

    @Test
    public void test_Inheritance_reusingInterfaces() {

        compileTestBuilder
                .andSourceFiles(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_Inheritance_reusingInterfaces.java"))
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .executeTest();


    }


    @Test
    public void test_externalOrSharedBB() {
        compileTestBuilder.andSourceFiles("/testcases/sharedBB/FirstApi.java", "/testcases/sharedBB/SharedBB.java")
                .whenCompiled()
                .thenExpectThat().compilationSucceeds()
                .executeTest();
    }

}