package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiInterface;

import javax.lang.model.element.Modifier;

public class CustomFluentApiInterfaceWrapperCode {

    @DeclareCompilerMessage(code="060", enumValueName = "ERROR_FLUENTAPI_INTERFACE_MUST_BE_DEFINED_IN_CLASS_ANNOTATED_WITH_FLUENT_API_ANNOTATION",message = "Interfaces annotated with ${0} must be located inside a class annotated with ${1}", processorClass = FluentApiProcessor.class)
    @CustomCodeMethod(FluentApiInterface.class)
    static boolean validate(FluentApiInterfaceWrapper wrapper) {

        // check if annotation is placed on class and if class is inside type
        return FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(AptkCoreMatchers.IS_INTERFACE)
                .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                .validateAndIssueMessages()
                &&
                FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement().getEnclosingElement())
                        .is(AptkCoreMatchers.IS_CLASS)
                        .setCustomMessage(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_INTERFACE_MUST_BE_DEFINED_IN_CLASS_ANNOTATED_WITH_FLUENT_API_ANNOTATION, FluentApiInterface.class.getName(), FluentApi.class.getName()).applyValidator(AptkCoreMatchers.BY_ANNOTATION).hasAllOf(FluentApi.class)
                        .validateAndIssueMessages()
                ;

    }



}
