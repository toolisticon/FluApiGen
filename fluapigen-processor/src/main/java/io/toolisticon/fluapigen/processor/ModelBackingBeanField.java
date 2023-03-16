package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;

import java.util.Optional;
import java.util.Set;


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


    @Override
    public Set<String> fetchImports() {
        return this.getFieldType().getImports();
    }

    @Override
    @DeclareCompilerMessage(code = "200", enumValueName = "ERROR_BACKING_BEAN_FIELD_MUST_BE_ANNOTATED_WITH_BB_FIELD_ANNOTATION", message = "Backing bean field method must be annotated with ${0} annotation", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "201", enumValueName = "ERROR_BACKING_BEAN_FIELD_ID_MUST_NOT_BE_EMPTY", message = "Backing bean field id must not be an empty string", processorClass = FluentApiProcessor.class)

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


        }

        return outcome;
    }
}
