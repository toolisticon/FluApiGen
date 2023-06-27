package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import java.util.Optional;
import java.util.stream.Collectors;

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
        parameterName = variableElement.getSimpleName();
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

    public String getAssignmentString() {

        Optional<ModelBackingBeanField> backBeanField = getBackingBeanField();

        if (backBeanField.isPresent()) {

            // must distinct two cases
            // 1. Collections - May have ADD or SET action
            // 2. Single Value - only set is allowed per default
            // Might also think about handling Arrays like Collections

            if (backBeanField.get().isCollection()) {

                switch (getFluentApiBackingBeanMapping().action()) {

                    case SET: {

                        // validate types
                        TypeMirrorWrapper typeMirrorWrapper = type;
                        if (typeMirrorWrapper.isCollection() || typeMirrorWrapper.isArray()) {
                            typeMirrorWrapper = typeMirrorWrapper.getWrappedComponentType();
                        }
                        if (!typeMirrorWrapper.erasure().isAssignableTo(backBeanField.get().getConcreteType().erasure())) {
                            // must throw validation error
                            throw new IncompatibleParameterTypeException(type.getSimpleName(),backBeanField.get().getFieldName(),backBeanField.get().getFieldType().getTypeDeclaration(),FluentApiBackingBeanMappingWrapper.wrap(parameterElement.unwrap()));
                        }

                        // must handle 3 scenarios depending on parameter type:

                        // 1. varargs / array
                        // 2. Collection
                        // 3. single value

                        if (type.isArray()) {
                            return " = new " + backBeanField.get().getCollectionImplType() + "(Arrays.asList(" + getParameterName() + "));";
                        } else if (type.isCollection()) {
                            return " = new " + backBeanField.get().getCollectionImplType() + "(" + getParameterName() + ");";
                        } else {
                            return " = new " + backBeanField.get().getCollectionImplType() + "(Collections.singletonList( " + getParameterName() + " ));";
                        }

                    }
                    case ADD: {
                        // must handle 3 scenarios depending on parameter type:

                        // 1. varargs / array
                        // 2. Collection
                        // 3. single value

                        if (type.isArray()) {
                            return ".addAll(Arrays.asList(" + getParameterName() + "));";
                        } else if (type.isCollection()) {
                            return ".addAll(" + getParameterName() + ");";
                        } else {
                            return ".add(" + getParameterName() + ");";
                        }

                    }
                }
            }
            /*-
            else if (backBeanField.get().isArray()) {
                switch (getFluentApiBackingBeanMapping().action()) {
                    case SET: {
                        return;
                    }
                    case ADD: {
                        return;
                    }
                }
            }*/

            else {

                // just parameter assignment
                return " = " + getParameterName() +";";
            }

        }

        return "";
    }



    @DeclareCompilerMessage(code = "001", enumValueName = "BB_MAPPING_ANNOTATION_MUST_BE_PRESENT", message = "${0} annotation must be present on parameter", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "002", enumValueName = "PARAMETER_AND_MAPPED_BB_FIELD_MUST_HAVE_SAME_TYPE", message = "Parameter type (${0}) must match backing bean field type (${1})", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "003", enumValueName = "BB_MAPPING_COULDNT_BE_RESOLVED", message = "Field ${0} doesn't exist in mapped backing bean ${1}. It must be one of: [${2}]", processorClass = FluentApiProcessor.class)
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

        if (!getBackingBeanField().isPresent()) {

            ModelBackingBean referencedBackingBean = fluentApiBackingBeanMapping.target() == TargetBackingBean.THIS ? backingBeanModel : modelInterfaceMethod.getNextBackingBean();
            parameterElement.compilerMessage().asError().write(
                    FluentApiProcessorCompilerMessages.BB_MAPPING_COULDNT_BE_RESOLVED,
                    fluentApiBackingBeanMapping.value(),
                    referencedBackingBean.getBackingBeanInterfaceSimpleName(),
                    referencedBackingBean.getFields().stream().map(e -> "\"" + e.getFieldId() + "\"").collect(Collectors.joining(", "))

            );
            return false;
        }


        try {
            getAssignmentString();
        } catch (IncompatibleParameterTypeException e) {
            e.writeErrorCompilerMessage();
            return false;
        }

        return result;
    }

}
