package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.aptk.tools.wrapper.ElementWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInlineBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.List;

public class CustomFluentApiInlineBackingBeanMappingWrapperCode {


    @DeclareCompilerMessage(code = "512", enumValueName = "ERROR_NO_INLINE_BB_MAPPING_MUST_BE_USED_IN_FLUENT_INTERFACE", message = "Anotation ${0} must be used in class annotated with ${1}", processorClass = FluentApiProcessor.class)

    @CustomCodeMethod(FluentApiInlineBackingBeanMapping.class)
    static boolean validate(FluentApiInlineBackingBeanMappingWrapper wrapper) {

        if (!FluentApiInterfaceWrapper.isAnnotated(wrapper._annotatedElement().getEnclosingElement())) {
            wrapper.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_NO_INLINE_BB_MAPPING_MUST_BE_USED_IN_FLUENT_INTERFACE,  FluentApiInlineBackingBeanMapping.class.getSimpleName(), FluentApiInterface.class.getSimpleName());
            return false;
        }

        return true;
    }


}
