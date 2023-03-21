package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.corematcher.CoreMatcherValidationMessages;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.JavaFileObjectUtils;
import io.toolisticon.cute.PassIn;
import io.toolisticon.cute.UnitTestForTestingAnnotationProcessors;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.FileWriter;


/**
 * Tests of {@link io.toolisticon.fluapigen.api.FluentApi}.
 *
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

/*-
    @Test
    public void test_valid_usage() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/TestcaseValidUsage.java"))
                .compilationShouldSucceed()
                .expectThatGeneratedSourceFileExists("io.toolisticon.fluapigen.processor.tests.Xyz")
                .executeTest();
    }


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
                //.compilationShouldFail()
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
    public void test_invalid_ImpllicitValue_Int() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_InvalidImplicitValue_Int.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE.getCode())
                .executeTest();

    }

    @Test
    public void test_invalid_MissingBBFieldAnnotation() {

        compileTestBuilder
                .addSources(JavaFileObjectUtils.readFromResource("testcases/IntegrationTest_MissingBBFieldAnnotation.java"))
                .compilationShouldFail()
                .expectErrorMessage().thatContains(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_MUST_BE_ANNOTATED_WITH_BB_FIELD_ANNOTATION.getCode())
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


}