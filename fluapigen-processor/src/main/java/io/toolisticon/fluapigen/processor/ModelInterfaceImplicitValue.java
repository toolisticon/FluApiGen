package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.InterfaceUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.fluapigen.api.FluentApiConverter;

import java.util.Arrays;
import java.util.List;
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
        if (!annotation.converterIsDefaultValue()) {
            if (this.backingBeanField.isCollection()) {

                switch (annotation.action()) {

                    case SET: {

                        if (annotation.value().length == 0) {
                            return " = new " + backingBeanField.getCollectionImplType() + "()";
                        } else {
                            String valueString = Arrays.stream(annotation.value()).map(e -> "new " + annotation.converterAsFqn() + "().convert(\"" + e + "\")").collect(Collectors.joining(", "));
                            return " = new " + backingBeanField.getCollectionImplType() + "(Arrays.asList(" + valueString + "))";
                        }

                    }
                    case ADD:
                    default: {

                        if (annotation.value().length == 0) {
                            return ".addAll(Arrays.asList(Collections.EMPTY_LIST))";
                        } else {
                            String valueString = Arrays.stream(annotation.value()).map(e -> "new " + annotation.converterAsFqn() + "().convert(\"" + e + "\")").collect(Collectors.joining(", "));
                            return ".addAll(Arrays.asList(" + valueString + "))";
                        }

                    }
                }
            } else if (this.backingBeanField.isArray()) {

                String valueString = Arrays.stream(annotation.value()).map(e -> "new " + annotation.converterAsFqn() + "().convert(\"" + e + "\")").collect(Collectors.joining(", "));
                return " = new " + this.backingBeanField.getFieldType().getWrappedComponentType().getSimpleName() + "[]" + "{ " + valueString + " }";

            } else {
                return " = new " + annotation.converterAsFqn() + "().convert(\"" + (annotation.value())[0] + "\")";
            }

        } else {
            return ModelBackingBeanField.getAssignmentString(backingBeanField.getFieldType(), annotation.value(), annotation.action());
        }
    }


    @Override
    @DeclareCompilerMessage(code = "400", enumValueName = "ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE", message = "Cannot convert value string '${0}' to target type '${1}'", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "401", enumValueName = "ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE", message = "Target type '${0}' is not supported", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "402", enumValueName = "ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE", message = "Enum '${0}' has no value '${1}'. Must be one of ${2}", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "403", enumValueName = "ERROR_IMPLICIT_VALUE_MUST_BE_SINGLE_VALUE", message = "Implicit value must be one single value", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "404", enumValueName = "ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING", message = "Implicit value converter must take String as source type but got ${0}", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "405", enumValueName = "ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE", message = "Implicit value converter must convert to target attribute type ${0} but found ${1}.", processorClass = FluentApiProcessor.class)

    public boolean validate() {

        try {

            if (!this.annotation.converterIsDefaultValue()) {
                List<TypeMirrorWrapper> typeParameters = InterfaceUtils.getResolvedTypeArgumentOfSuperTypeOrInterface(this.annotation.converterAsTypeMirrorWrapper().getTypeElement().get(), TypeMirrorWrapper.wrap(FluentApiConverter.class));
                // must check if converter matches source and target type
                if (typeParameters.size() == 2) {

                    if (!TypeMirrorWrapper.wrap(String.class).equals(typeParameters.get(0))) {
                        annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_SOURCE_TYPE_STRING, typeParameters.get(0).getSimpleName());
                    }

                    TypeMirrorWrapper targetType = typeParameters.get(1);
                    TypeMirrorWrapper targetBackingBeanFieldType = this.backingBeanField.getFieldType().isArray() || this.backingBeanField.getFieldType().isCollection() ? this.backingBeanField.getFieldType().getWrappedComponentType() : this.backingBeanField.getFieldType();

                    if (!targetType.erasure().isAssignableTo(targetBackingBeanFieldType.erasure())) {
                        annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CONVERTER_MUST_CONVERT_TO_TARGET_ATTRIBUTE_TYPE, targetBackingBeanFieldType.getSimpleName(), targetType.getSimpleName());
                    }
                }


            }

            getValueAssignmentString();
            return true;
        } catch (NumberFormatException e) {
            annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE, annotation.value(), backingBeanField.getFieldType().getSimpleName());
        } catch (ModelBackingBeanField.UnsupportedTypeException e) {
            annotation.idAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE, e.unsupportedType);
        } catch (ModelBackingBeanField.InvalidEnumConstantException e) {
            annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE, e.enumType, e.enumValue, e.validEnumValues);
        } catch (ModelBackingBeanField.IllegalNumberOfValuesException e) {
            annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_MUST_BE_SINGLE_VALUE);
        }

        return false;

    }

}
