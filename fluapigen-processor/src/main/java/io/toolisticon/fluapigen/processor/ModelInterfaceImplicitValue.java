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
        return backingBeanField.getValueAssignmentString(annotation.value());
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
        } catch (ModelBackingBeanField.UnsupportedTypeException e) {
            annotation.idAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE, e.unsupportedType);
        } catch (ModelBackingBeanField.InvalidEnumConstantException e) {
            annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE, e.enumType, e.enumValue, e.validEnumValues);
        }

        return false;

    }

}
