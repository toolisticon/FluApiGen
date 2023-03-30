package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.aptk.tools.wrapper.ElementWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.List;

public class CustomFluentApiCommandWrapperCode {

    @CustomCodeMethod(FluentApiCommand.class)
    static String getCommandMethodName(FluentApiCommandWrapper wrapper) {

        if (wrapper._annotatedElement().getKind() == ElementKind.METHOD) {
            return wrapper.valueAsTypeMirrorWrapper().getTypeElement().get().filterEnclosedElements()
                    .applyFilter(AptkCoreMatchers.IS_METHOD).getResult().get(0).getSimpleName().toString();
        } else {
            return TypeElementWrapper.wrap(wrapper._annotatedElement()).filterEnclosedElements().applyFilter(AptkCoreMatchers.IS_METHOD).getResult().get(0).getSimpleName().toString();
        }

    }

    @DeclareCompilerMessage(code = "065", enumValueName = "ERROR_COMMAND_CLASS_MUST_BE_DEFINED_IN_CLASS_ANNOTATED_WITH_FLUENT_API_ANNOTATION", message = "Static command class annotated with ${0} must be nested inside a class annotated with ${1}", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "066", enumValueName = "ERROR_COMMAND_CLASS_MUST_DECLARE_EXACTLY_ONE_STATIC_METHOD", message = "Class must contain exactly one static method", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "067", enumValueName = "ERROR_PARAMETER_OF_COMMAND_METHOD_MUST_BE_INTERFACE_ANNOTATED_AS_BACKING_BEAN", message = "The parameter must reference an interface annotated with ${0} annotation", processorClass = FluentApiProcessor.class)
    @CustomCodeMethod(FluentApiCommand.class)
    static boolean validate(FluentApiCommandWrapper wrapper) {


        if (wrapper._annotatedElement().getKind() == ElementKind.METHOD) {
            // CASE 1 : REFERENCE IN FLUENT API INTERFACE
            return FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                    .is(AptkCoreMatchers.IS_METHOD)
                    .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                    .validateAndIssueMessages()
                    &
                    FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement().getEnclosingElement())
                            .is(AptkCoreMatchers.IS_INTERFACE)
                            .validateAndIssueMessages();

        } else {
            // CASE 2 : REFERENCE ON COMMAND CLASS
            // check if annotation is placed on class and if class is inside type
            boolean result = FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                    .is(AptkCoreMatchers.IS_CLASS)
                    .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                    .validateAndIssueMessages()
                    &
                    FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement().getEnclosingElement())
                            .is(AptkCoreMatchers.IS_CLASS)
                            .setCustomMessage(FluentApiProcessorCompilerMessages.ERROR_COMMAND_CLASS_MUST_BE_DEFINED_IN_CLASS_ANNOTATED_WITH_FLUENT_API_ANNOTATION, FluentApiInterface.class.getName(), FluentApi.class.getName()).applyValidator(AptkCoreMatchers.BY_ANNOTATION).hasAllOf(FluentApi.class)
                            .validateAndIssueMessages();

            // check if class exactly contains one static function.
            List<ExecutableElement> methods = ElementWrapper.wrap(wrapper._annotatedElement()).filterEnclosedElements().applyFilter(AptkCoreMatchers.IS_METHOD).getResult();
            if (methods.size() != 1) {
                MessagerUtils.error(wrapper._annotatedElement(), FluentApiProcessorCompilerMessages.ERROR_COMMAND_CLASS_MUST_DECLARE_EXACTLY_ONE_STATIC_METHOD);
                return false;
            } else {
                result = result & FluentElementValidator.createFluentElementValidator(methods.get(0))
                        .is(AptkCoreMatchers.IS_METHOD)
                        .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasAllOf(Modifier.STATIC)
                        .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                        .applyValidator(AptkCoreMatchers.BY_NUMBER_OF_PARAMETERS).hasOneOf(1)
                        .validateAndIssueMessages();


                // First parameter must be a BackingBean type from same class as annotated element
                if (result) {
                    VariableElementWrapper firstParameter = ExecutableElementWrapper.wrap(methods.get(0)).getParameters().get(0);
                    if (firstParameter.asType().isPrimitive() || !firstParameter.asType().getTypeElement().get().hasAnnotation(FluentApiBackingBean.class)) {
                        //FluentApiProcessorCompilerMessages.
                        FluentApiProcessorCompilerMessages.ERROR_PARAMETER_OF_COMMAND_METHOD_MUST_BE_INTERFACE_ANNOTATED_AS_BACKING_BEAN.error(firstParameter.unwrap(), FluentApiBackingBean.class.getSimpleName());
                        result = false;
                    }
                }
            }


            return result;

        }

    }


}
