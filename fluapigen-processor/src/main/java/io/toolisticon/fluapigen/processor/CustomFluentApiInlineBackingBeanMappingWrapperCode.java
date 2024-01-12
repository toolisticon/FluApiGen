package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.fluapigen.api.FluentApiInlineBackingBeanMapping;

public class CustomFluentApiInlineBackingBeanMappingWrapperCode {


    @DeclareCompilerMessage(code = "512", enumValueName = "ERROR_NO_INLINE_BB_MAPPING_MUST_BE_USED_IN_FLUENT_INTERFACE", message = "Anotation ${0} must be used in class annotated with ${1}", processorClass = FluentApiProcessor.class)

    @CustomCodeMethod(FluentApiInlineBackingBeanMapping.class)
    static boolean validate(FluentApiInlineBackingBeanMappingWrapper wrapper) {

/*-
        // THIS IS NOT TRUE FOR SUPERCLASSES!!!
        if (!FluentApiInterfaceWrapper.isAnnotated(wrapper._annotatedElement().getEnclosingElement())) {
            wrapper.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_NO_INLINE_BB_MAPPING_MUST_BE_USED_IN_FLUENT_INTERFACE,  FluentApiInlineBackingBeanMapping.class.getSimpleName(), FluentApiInterface.class.getSimpleName());
            return false;
        }
*/


        return true;
    }


}
