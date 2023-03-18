package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;

import javax.lang.model.element.ElementKind;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class ModelBackingBeanField implements FetchImports, Validatable {

    /**
     * field is represented by an interface function.
     */
    private final ExecutableElementWrapper field;

    private final FluentApiBackingBeanFieldWrapper annotation;

    ModelBackingBeanField(ExecutableElementWrapper field) {
        this.field = field;
        this.annotation = FluentApiBackingBeanFieldWrapper.wrap(field.unwrap());
    }

    public FluentApiBackingBeanFieldWrapper getAnnotation() {
        return annotation;
    }

    /**
     * Get the type of the field.
     *
     * @return the fields type as a TypeMirrorWrapper
     */
    public TypeMirrorWrapper getFieldType() {
        return field.getReturnType();
    }

    /**
     * Gets the concrete type of the field.
     * This is the component type for collections or otherwise the field type.
     *
     * @return the component type for collections or otherwise the field type
     */

    public TypeMirrorWrapper getConcreteType() {
        return getFieldType().isCollection() ? getFieldType().getWrappedComponentType() : getFieldType();
    }

    public String getFieldName() {
        return field.getSimpleName();
    }

    public String getFieldId() {
        return annotation != null ? annotation.value() : null;
    }

    public String getGetterMethodSignature() {
        return this.field.getMethodSignature();
    }

    /**
     * Checks whether the field type is a collection or not.
     *
     * @return true if field type is a Collection, otherwise not
     */
    public boolean isCollection() {
        return this.getFieldType().isCollection();
    }

    /**
     * Checks whether the field type is an array or not.
     *
     * @return true if field type is an array, otherwise not
     */
    public boolean isArray() {
        return this.getFieldType().isArray();
    }

    public boolean isCloneable() {

        if (isCollection()) {
            return this.getFieldType().getWrappedComponentType().isAssignableTo(Cloneable.class);
        } else {
            return this.getFieldType().isAssignableTo(Cloneable.class);
        }

    }

    public boolean isBackingBeanReference() {

        return isCollection() ? this.getFieldType().getWrappedComponentType().getTypeElement().isPresent()
                && this.getFieldType().getWrappedComponentType().getTypeElement().get().hasAnnotation(FluentApiBackingBean.class) :
                this.getFieldType().getTypeElement().isPresent()
                        && this.getFieldType().getTypeElement().get().hasAnnotation(FluentApiBackingBean.class);

    }

    /**
     * Returns the fields Backing Bean reference if field is related to other backing bean type.
     *
     * @return The interface name of the referenced backing bean or an empty Optional
     */
    public Optional<String> getBackingBeanReference() {
        return isBackingBeanReference() ?
                Optional.of(
                        isCollection() ?
                                this.getFieldType().getWrappedComponentType().getTypeElement().get().getSimpleName() :
                                this.getFieldType().getTypeElement().get().getSimpleName()
                ) :
                Optional.empty();
    }

    /**
     * Gets the Backing Bean Model if the field is a backing bean.
     *
     * @return
     */
    public ModelBackingBean getBackingBeanReferenceBackingBeanModel() {
        Optional<String> backingBeanReferenceSimpleName = getBackingBeanReference();
        return backingBeanReferenceSimpleName.isPresent() ? RenderStateHelper.getBackingBeanModelForBackingBeanInterfaceSimpleName(backingBeanReferenceSimpleName.get()) : null;
    }

    @Override
    public Set<String> fetchImports() {
        return this.getFieldType().getImports();
    }

    public String getInitValueString() {

        // return empty string for default value
        if (this.getAnnotation().initValueIsDefaultValue()) {
            return "";
        }

        String valueAssignementString = getValueAssignmentString(getFieldType(), getAnnotation().initValue(), false);
        return valueAssignementString.isEmpty() ? "" : " = " + valueAssignementString;
    }

    public static String getValueAssignmentString(TypeMirrorWrapper fieldTMW, String[] values, boolean isAssignment) {

        if (fieldTMW.isCollection()) {

            String ImplementationType = getCollectionImplType(fieldTMW);
            if (values.length == 0) {
                return "new " + ImplementationType + "()";
            } else {
                String valueString = String.join(", ", Arrays.stream(values).map(e -> getValueAssignmentStringForSingleValue(fieldTMW.getWrappedComponentType(), e)).collect(Collectors.toList()));
                return "new " + ImplementationType + "(Arrays.asList(" + valueString + "))";
            }
        }

        if(fieldTMW.isArray()) {
                String valueString = String.join(", ", Arrays.stream(values).map(e -> getValueAssignmentStringForSingleValue(fieldTMW.getWrappedComponentType(), e)).collect(Collectors.toList()));
                return (isAssignment ? "new " + fieldTMW.getWrappedComponentType().getSimpleName() + "[]" : "") + "{ " + valueString +  " }";

        }

        // expect single value here
        if (values.length != 1) {
            throw new IllegalNumberOfValuesException();
        }

        return getValueAssignmentStringForSingleValue(fieldTMW, values[0]);

    }
    
    public String getCollectionImplType() {
        return getCollectionImplType(getFieldType());
    }

    static String getCollectionImplType(TypeMirrorWrapper tmw){
        // TODO: must check if types are really the same :  Types.isSameType(TypeMirror, TypeMirror)
        if (tmw.erasure().isAssignableTo(List.class)){
            return "ArrayList";
        } else if (tmw.erasure().isAssignableTo(Set.class)) {
            return "HashSet";
        } else {
            throw new UnsupportedTypeException(tmw.getQualifiedName());
        }
    }

    public static String getValueAssignmentStringForSingleValue(TypeMirrorWrapper fieldTMW, String valueStr) {
        if (fieldTMW.isEnum()) {
            // handle enum
            return handleEnum(fieldTMW, valueStr);
        } else {

            switch (fieldTMW.getQualifiedName()) {
                case "java.lang.String": {
                    return "\"" + valueStr + "\"";
                }
                case "boolean":
                case "java.lang.Boolean": {
                    return Boolean.valueOf(valueStr).toString();
                }
                case "int":
                case "java.lang.Integer": {
                    return Integer.valueOf(valueStr).toString();
                }
                case "long":
                case "java.lang.Long": {
                    return Long.valueOf(valueStr).toString() + "L";
                }
                case "float":
                case "java.lang.Float": {
                    return Float.valueOf(valueStr).toString() + "f";
                }
                case "double":
                case "java.lang.Double": {
                    return Float.valueOf(valueStr).toString();
                }
                default: {
                    throw new UnsupportedTypeException(fieldTMW.getQualifiedName());
                }

            }

        }
    }

    private static String handleEnum(TypeMirrorWrapper fieldTMW, String valueStr) {
        Set<String> enumValues = fieldTMW.getTypeElement().get().getEnclosedElements().stream().filter(e -> e.unwrap().getKind() == ElementKind.ENUM_CONSTANT).map(e -> e.getSimpleName()).collect(Collectors.toSet());
        if (!enumValues.contains(valueStr)) {
            throw new InvalidEnumConstantException(fieldTMW.getSimpleName(), valueStr, enumValues);
        }

        return fieldTMW.getSimpleName() + "." + valueStr;
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

    static class UnsupportedTypeException extends RuntimeException {

        final String unsupportedType;

        public UnsupportedTypeException(String unsupportedType) {
            this.unsupportedType = unsupportedType;
        }
    }

    static class IllegalNumberOfValuesException extends RuntimeException {

    }

    @Override
    @DeclareCompilerMessage(code = "200", enumValueName = "ERROR_BACKING_BEAN_FIELD_MUST_BE_ANNOTATED_WITH_BB_FIELD_ANNOTATION", message = "Backing bean field method must be annotated with ${0} annotation", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "201", enumValueName = "ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_BE_EMPTY", message = "Backing bean field id must not be an empty string", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "202", enumValueName = "ERROR_BACKING_BEAN_FIELD_INIT_VALUE_MUST_BE_SINGLE_VALUE", message = "Backing bean fields init value must be one single value", processorClass = FluentApiProcessor.class)

    public boolean validate() {
        boolean outcome = true;

        if (annotation == null) {
            // must be annotated with FluentApiBackingBeanField annotation
            FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_MUST_BE_ANNOTATED_WITH_BB_FIELD_ANNOTATION.error(field.unwrap(), FluentApiBackingBeanField.class.getSimpleName());
            outcome = false;
        } else {

            // id must be not empty
            if ("".equals(annotation.value())) {
                annotation.compilerMessage()
                        .asError()
                        .write(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_BE_EMPTY);
                outcome = false;
            }


            try {
                getInitValueString();

            } catch (NumberFormatException e) {
                annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_CANNOT_CONVERT_VALUE_STRING_TO_TARGET_TYPE, annotation.initValue(), this.getFieldType().getSimpleName());
                outcome = false;
            } catch (ModelBackingBeanField.UnsupportedTypeException e) {
                annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_UNSUPPORTED_TYPE, e.unsupportedType);
                outcome = false;
            } catch (ModelBackingBeanField.InvalidEnumConstantException e) {
                annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_IMPLICIT_VALUE_INVALID_ENUM_VALUE, e.enumType, e.enumValue, e.validEnumValues);
                outcome = false;
            } catch (IllegalNumberOfValuesException e) {
                annotation.valueAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_BACKING_BEAN_FIELD_INIT_VALUE_MUST_BE_SINGLE_VALUE);
                outcome = false;
            }

        }

        return outcome;
    }


    public static void main(String[] args) {
        String[] wtf = {"a", "b", "C"};

        List<String> wtfList = new ArrayList<>(Arrays.asList("a", "b", "c"));
        wtfList.add("d");
        System.out.println(wtfList);

    }
}
