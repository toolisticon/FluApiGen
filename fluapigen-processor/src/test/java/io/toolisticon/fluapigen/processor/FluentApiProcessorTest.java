package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.JavaFileObjectUtils;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests of {@link io.toolisticon.fluapigen.api.FluentApi}.
 * <p>
 * TODO: replace the example testcases with your own testcases
 */

public class FluentApiProcessorTest {


    CompileTestBuilder.CompilationTestBuilder compileTestBuilder;

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);

        compileTestBuilder = CompileTestBuilder
                .compilationTest()
                .addProcessors(FluentApiProcessor.class);
    }


    @Test
    public void test_valid_usage() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/TestcaseValidUsage.java"))
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.Xyz")
                .executeTest();
    }

    @Test
    public void test_valid_usage_with_inline_bb_mapping() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/TestcaseValidUsageWithInlineBackingBeanMapping.java"))
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.Xyz")
                .executeTest();
    }


/*-
    @Test
    public void test_valid_usage2() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/ExampleFluentApi.java"))
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.ExampleFluentApiStarter")
                .executeTest();
    }
*/

    @Test
    public void test_valid_usage3() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/CuteFluentApi.java"))
                .compilationShouldSucceed()
                //.expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.ExampleFluentApiStarter")
                .executeTest();
    }

    @Test
    public void test_valid_usage4() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest.java"))
                //.compilationShouldFail()
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.testcases.IntegrationTestStarter")
                .executeTest();
    }

    @Test
    public void test_valid_usage_withDefaultMethods() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/TestcaseValidUsageWithDefaultMethods.java"))
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.Xyz")
                .executeTest();
    }

    @Test
    public void test_invalid_bbFieldNameMapping() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidBBFieldMapping.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_CANNOT_FIND_BACKING_BEAN_FIELD.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_ReturnTypeInFluentInterface() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidReturnTypeInFluentInterface.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_RETURN_TYPE_MUST_BE_FLUENT_INTERFACE.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_MappingAtParentTraversal() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidMappingAtParentTraversal.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_CANNOT_FIND_BACKING_BEAN_FIELD.getCode())
                .executeTest();

    }


    @Test
    public void test_invalid_MissingMappingAnnotationOnParentTraversal() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_MissingMappingAnnotationOnParentTraversal.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT_ADD_TO_PARENT_TRAVERSALS.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_ParameterAtCommand() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidParameterAtCommand.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_PARAMETER_OF_COMMAND_METHOD_MUST_BE_INTERFACE_ANNOTATED_AS_BACKING_BEAN.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_NoParameterAtCommand() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_NoParameterAtCommand.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(CoreMatcherValidationMessages.BY_NUMBER_OF_PARAMETERS.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_MultipleRootInterfaces() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidMultipleRootInterfaces.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_MULTIPLE_ROOT_INTERFACES.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_ImplicitValue_Int() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidImplicitValue_Int.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE.getCode())
                .executeTest();

    }

    @Test
    public void test_valid_MissingBBFieldAnnotation() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_MissingBBFieldAnnotation.java"))
                .compilationShouldSucceed()
                .executeTest();

    }

    @Test
    public void test_invalid_NonUniqueBBFieldId() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_NonUniqueBBFieldId.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_UNIQUE_IN_BB.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_NonUniqueBBFieldId_ImplicitlySet() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_NonUniqueBBFieldId_ImplicitlySet.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_UNIQUE_IN_BB.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_EmptyBBFieldId() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_EmptyBBFieldId.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_BE_EMPTY.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_MissingMethodParameterBBMapping() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_MissingMethodParameterBBMapping.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_UnsupportedImplicitValueType() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_UnsupportedImplicitValueType.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_InvalidImplicitValue_Enum() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidImplicitValue_Enum.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE.getCode())
                .executeTest();


    }

    @Test
    public void test_invalid_InvalidNumberOfParentMappingsAtCommand() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/TestcaseInvalidNumberOfParentMappingsAtCommand.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_INVALID_NUMBER_OF_PARENT_MAPPINGS_TO_REACH_ROOT_BB.getCode())
                .executeTest();


    }

    @Test
    public void test_valid_ImplicitValueConverter() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_ImplicitValueConverterTest.java"))
                .compilationShouldSucceed()
                .executeTest();


    }

    @Test
    public void test_invalid_ImplicitValueConverter_invalidTargetType() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java"))
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLineNumber(55L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLineNumber(58L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLineNumber(61L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidTargetType.java").atLineNumber(64L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE.getCode())
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void test_invalid_ImplicitValueConverter_invalidSourceType() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java"))
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLineNumber(55L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLineNumber(58L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLineNumber(61L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .expectErrorMessage().atSource("/testcases/IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType.java").atLineNumber(64L).thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING.getCode())
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void test_valid_BackingBeanMappingConverter() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter.java"))
                .compilationShouldSucceed()
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongSingleValueSource() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongSourceSingleValue.java"))
                .expectErrorMessage().atLineNumber(62L).thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongArrayValueSource() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongSourceArrayValue.java"))
                .expectErrorMessage().atLineNumber(64L).thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongCollectionValueSource() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongSourceCollectionValue.java"))
                .expectErrorMessage().atLineNumber(66L).thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongSingleValueTarget() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongTargetSingleValue.java"))
                .expectErrorMessage().atLineNumber(62L).thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongArrayValueTarget() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongTargetArrayValue.java"))
                .expectErrorMessage().atLineNumber(64L).thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .compilationShouldFail()
                .executeTest();


    }

    @Test
    public void test_invalid_BackingBeanMappingConverter_wrongCollectionValueTarget() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_BackingBeanMapping_Converter_WrongTargetCollectionValue.java"))
                .expectErrorMessage().atLineNumber(66L).thatContains(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER.getCode())
                .compilationShouldFail()
                .executeTest();


    }

}