package io.toolisticon.fluapigen.processor;


import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

@DeclareCompilerMessage(code = "601", enumValueName = "ERROR_CANNOT_FIND_NEXT_BACKING_BEAN", message = "Cannot find next backing bean at method : '${0}'", processorClass = FluentApiProcessor.class)
public class BBNotFoundException extends BaseException{

    final ExecutableElementWrapper executableElementWrapper;

    public BBNotFoundException(ExecutableElementWrapper executableElementWrapper) {
        this.executableElementWrapper = executableElementWrapper;

    }


    public void writeErrorCompilerMessage() {
        MessagerUtils.error(executableElementWrapper.unwrap(), FluentApiProcessorCompilerMessages.ERROR_CANNOT_FIND_NEXT_BACKING_BEAN, executableElementWrapper.getMethodSignature());
    }

}
