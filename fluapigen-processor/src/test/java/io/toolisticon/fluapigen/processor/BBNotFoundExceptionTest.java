package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.cute.CompileTestBuilder;
import io.toolisticon.cute.PassIn;
import org.junit.Before;
import org.junit.Test;

import javax.lang.model.element.ExecutableElement;

/**
 * Unit Test for {@link BBNotFoundException}.
 */
public class BBNotFoundExceptionTest {

    @Before
    public void init() {
        MessagerUtils.setPrintMessageCodes(true);
    }

    interface ExceptionTest {
        @PassIn
        String method();

    }

    @Test
    public void testWriteCompilerMessage() {

        CompileTestBuilder.unitTest().<ExecutableElement>defineTestWithPassedInElement(ExceptionTest.class, (processingEnvironment, element) -> {


                    try {
                        ToolingProvider.setTooling(processingEnvironment);

                        ExecutableElementWrapper executableElementWrapper = ExecutableElementWrapper.wrap(element);
                        BBNotFoundException unit = new BBNotFoundException(executableElementWrapper);

                        unit.writeErrorCompilerMessage();
                    } finally {
                        ToolingProvider.clearTooling();
                    }
                })
                .compilationShouldFail()
                .expectErrorMessageThatContains(FluentApiProcessorCompilerMessages.ERROR_CANNOT_FIND_NEXT_BACKING_BEAN.getCode(), "String method()").executeTest();

    }

}
