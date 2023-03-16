package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import java.util.Optional;

public class ModelInterfaceMethodParameter {

    final VariableElementWrapper parameterElement;

    private final ModelBackingBean backingBeanModel;

    private final ModelInterfaceMethod modelInterfaceMethod;

    final String parameterName;

    final TypeMirrorWrapper type;

    final FluentApiBackingBeanMapping fluentApiBackingBeanMapping;

    ModelInterfaceMethodParameter(VariableElementWrapper variableElement, ModelBackingBean backingBeanModel, ModelInterfaceMethod modelInterfaceMethod) {
        parameterElement = variableElement;
        this.backingBeanModel = backingBeanModel;
        this.modelInterfaceMethod = modelInterfaceMethod;
        parameterName = variableElement.getSimpleName().toString();
        fluentApiBackingBeanMapping = variableElement.unwrap().getAnnotation(FluentApiBackingBeanMapping.class);
        type = variableElement.asType();
    }


    public Optional<ModelBackingBeanField> getBackingBeanField() {

        Optional<ModelBackingBeanField> returnValue = Optional.empty();

        if (fluentApiBackingBeanMapping != null) {

            switch (fluentApiBackingBeanMapping.target()) {
                case THIS: {
                    returnValue =  backingBeanModel.getFieldById(fluentApiBackingBeanMapping.value());
                    break;
                }
                case NEXT: {
                    returnValue = modelInterfaceMethod.getNextBackingBean().getFieldById(fluentApiBackingBeanMapping.value());
                    break;
                }
            }

        }

        return returnValue;
    }

    public FluentApiBackingBeanMapping getFluentApiBackingBeanMapping() {
        return fluentApiBackingBeanMapping;
    }

    public String getParameterName() {
        return parameterName;
    }

    @DeclareCompilerMessage(code = "001", enumValueName = "BB_MAPPING_ANNOTATION_MUST_BE_PRESENT", message = "${0} annotation must be present on parameter", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "002", enumValueName = "PARAMETER_AND_MAPPED_BB_FIELD_MUST_HAVE_SAME_TYPE", message = "Parameter type (${0}) must match backing bean field type (${1})", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "003", enumValueName = "BB_MAPPING_COULDNT_BE_RESOLVED", message = "Field ${0} doesn't exist in mapped backing bean ${1}", processorClass = FluentApiProcessor.class)
    public boolean validate() {

        boolean result = true;
        // fluentApiBackingBeanMapping must not be null -> missing annotation
        if (fluentApiBackingBeanMapping == null) {
            parameterElement.compilerMessage().asError().write(
                    FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT,
                    FluentApiBackingBeanMapping.class.getSimpleName()
            );
            return false;
        }

        // type must match backing bean field type - For collections its
        if (!getBackingBeanField().isPresent()) {
            parameterElement.compilerMessage().asError().write(
                    FluentApiProcessorCompilerMessages.BB_MAPPING_COULDNT_BE_RESOLVED,
                    fluentApiBackingBeanMapping.value(),
                    fluentApiBackingBeanMapping.target() == TargetBackingBean.THIS ? backingBeanModel.getBackingBeanInterfaceSimpleName() : modelInterfaceMethod.getNextBackingBean().getBackingBeanInterfaceSimpleName()
            );
            return false;
        }

        return result;
    }

}
