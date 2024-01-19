package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.PassIn;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit Test for {@link IncompatibleParameterTypeException}.
 */
public class IncompatibleParameterTypeExceptionTest {

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);
    }

    interface ExceptionTest {
        String method(@PassIn @FluentApiBackingBeanMapping("target") String parameter);

    }

    @Test
    public void testWriteCompilerMessage() {

        final String parameterType = "PT";
        final String bbFieldName = "FN";
        final String bbFieldType = "FT";
        CompileTestBuilder.unitTest().defineTestWithPassedInElement(ExceptionTest.class, (processingEnvironment, element) -> {


                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        FluentApiBackingBeanMappingWrapper fluentApiBackingBeanMappingWrapper = FluentApiBackingBeanMappingWrapper.wrap(element);
                        IncompatibleParameterTypeException unit = new IncompatibleParameterTypeException(parameterType, bbFieldName, bbFieldType, fluentApiBackingBeanMappingWrapper);

                        unit.writeErrorCompilerMessage();
                    } finally {
                        ToolingProvider.clearTooling();
                    }
                })
                .compilationShouldFail()
                .expectErrorMessageThatContains(FluentApiProcessorCompilerMessages.ERROR_INCOMPATIBLE_BACKING_BEAN_MAPPING_TYPES.getCode(), parameterType, bbFieldName, bbFieldType).executeTest();

    }

}
