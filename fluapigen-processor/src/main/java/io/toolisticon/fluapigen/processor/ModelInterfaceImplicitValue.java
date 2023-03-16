package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;

import javax.lang.model.element.ElementKind;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelInterfaceImplicitValue implements Validatable {


    private final ModelBackingBeanField backingBeanField;

    private final FluentApiImplicitValueWrapper annotation;

    public ModelInterfaceImplicitValue(ModelBackingBeanField backingBeanField, FluentApiImplicitValueWrapper annotation) {
        this.backingBeanField = backingBeanField;
        this.annotation = annotation;
    }

    public String getBackingBeanFieldName() {
        return backingBeanField.getFieldName();
    }

    public String getValueAssignmentString() {
        if (backingBeanField.getFieldType().isEnum()) {
            // handle enum
            return handleEnum();
        } else {

            switch (backingBeanField.getFieldType().getQualifiedName()) {
                case "java.lang.String": {
                    return "\"" + annotation.value() + "\"";
                }
                case "boolean":
                case "java.lang.Boolean": {
                    return Boolean.valueOf(annotation.value()).toString();
                }
                case "int":
                case "java.lang.Integer": {
                    return Integer.valueOf(annotation.value()).toString();
                }
                case "long":
                case "java.lang.Long": {
                    return Long.valueOf(annotation.value()).toString() + "L";
                }
                case "float":
                case "java.lang.Float": {
                    return Float.valueOf(annotation.value()).toString() + "f";
                }
                case "double":
                case "java.lang.Double": {
                    return Float.valueOf(annotation.value()).toString();
                }
                default: {
                    throw new UnsupportedTypeException(backingBeanField.getFieldType().getQualifiedName());
                }

            }

        }
    }

    private String handleEnum() {
        Set<String> enumValues = backingBeanField.getFieldType().getTypeElement().get().getEnclosedElements().stream().filter(e -> e.unwrap().getKind() == ElementKind.ENUM_CONSTANT).map(e -> e.getSimpleName()).collect(Collectors.toSet());
        if (!enumValues.contains(annotation.value())) {
            throw new InvalidEnumConstantException(backingBeanField.getFieldType().getSimpleName(), annotation.value(), enumValues);
        }

        return backingBeanField.getFieldType().getSimpleName() + "." + annotation.value();
    }

    @Override
    @DeclareCompilerMessage(code = "400", enumValueName = "ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE", message = "Cannot convert value string '${0}' to target type '${1}'", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "401", enumValueName = "ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE", message = "Target type '${0}' is not supported", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "402", enumValueName = "ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE", message = "Enum '${0}' has no value '${1}'. Must be one of ${2}", processorClass = FluentApiProcessor.class)
    public boolean validate() {

        try {
            getValueAssignmentString();
            return true;
        } catch (NumberFormatException e) {
            annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE, annotation.value(), backingBeanField.getFieldType().getSimpleName());
        } catch (UnsupportedTypeException e) {
            annotation.idAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE, e.unsupportedType);
        } catch (InvalidEnumConstantException e) {
            annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE, e.enumType, e.enumValue, e.validEnumValues);
        }

        return false;

    }

    static class InvalidEnumConstantException extends RuntimeException {

        final String enumValue;
        final String enumType;
        final Set<String> validEnumValues;

        public InvalidEnumConstantException(String enumType, String enumValue, Set<String> validEnumValues) {
            this.enumValue = enumValue;
            this.enumType = enumType;
            this.validEnumValues = validEnumValues;
        }
    }

    static class UnsupportedTypeException extends RuntimeException{

        final String unsupportedType;

        public UnsupportedTypeException(String unsupportedType) {
            this.unsupportedType = unsupportedType;
        }
    }

}
