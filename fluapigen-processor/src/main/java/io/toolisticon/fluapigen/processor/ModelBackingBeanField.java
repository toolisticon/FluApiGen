package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;

import java.util.Optional;
import java.util.Set;


public class ModelBackingBeanField implements FetchImports {

    /**
     * field is represented by an interface function.
     */
    private final ExecutableElementWrapper field;

    private final FluentApiBackingBeanField annotation;

    ModelBackingBeanField(ExecutableElementWrapper field) {
        this.field = field;
        this.annotation = field.getAnnotation(FluentApiBackingBeanField.class).get();
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
     * @return the component type for collections or otherwise the field type
     */

    public TypeMirrorWrapper getConcreteType() {
        return getFieldType().isCollection() ? getFieldType().getWrappedComponentType() : getFieldType();
    }

    public String getFieldName() {
        return field.getSimpleName();
    }

    public String getFieldId() {
        return annotation.value();
    }

    public String getGetterName() {
        // this is equal to the function name
        return field.getSimpleName();
    }

    public String getSetterName() {
        return "set" + getCapitalizedFieldName();
    }

    public String getAddName() {
        return "add" + getCapitalizedFieldName();
    }

    public String getClearName() {
        return "clear" + getCapitalizedFieldName();
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

    public boolean isBackingBeanReference() {

        return isCollection() ? this.getFieldType().getWrappedComponentType().getTypeElement().isPresent()
                && this.getFieldType().getWrappedComponentType().getTypeElement().get().hasAnnotation(FluentApiBackingBean.class):
                this.getFieldType().getTypeElement().isPresent()
                        && this.getFieldType().getTypeElement().get().hasAnnotation(FluentApiBackingBean.class);

    }

    /**
     * Returns the fields Backing Bean reference if field is related to other backing bean type.
     * @return The interface name of the referenced backing bean or an empty Optional
     */
    public Optional<String> getBackingBeanReference() {
        return isBackingBeanReference() ?
                Optional.of(
                        isCollection() ?
                                this.getFieldType().getWrappedComponentType().getTypeElement().get().getSimpleName():
                                this.getFieldType().getTypeElement().get().getSimpleName()
                ):
                Optional.empty();
    }


    /**
     * Returns the capitalized field name.
     *
     * @return the capitalized field name
     */
    private String getCapitalizedFieldName() {
        String fieldName = getFieldName();
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    @Override
    public Set<String> fetchImports() {
        return this.getFieldType().getImports();
    }

}
