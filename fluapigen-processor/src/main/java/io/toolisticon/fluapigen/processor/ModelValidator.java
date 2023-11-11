package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.wrapper.AnnotationMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.AnnotationValueWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ModelValidator {


    private final VariableElementWrapper annotatedElement;
    private final AnnotationMirrorWrapper validatorAnnotation;

    public ModelValidator(VariableElementWrapper annotatedElement, AnnotationMirrorWrapper validatorAnnotation) {
        this.annotatedElement = annotatedElement;
        this.validatorAnnotation = validatorAnnotation;
    }

    FluentApiValidatorWrapper getValidatorAnnotation() {
        return FluentApiValidatorWrapper.wrap(this.validatorAnnotation.asElement().unwrap());
    }


    @DeclareCompilerMessage(code = "752", enumValueName = "ERROR_BROKEN_VALIDATOR_ATTRIBUTE_NAME_MISMATCH", message = "The configured validator ${0} seems to have a broken annotation attribute to validator constructor parameter mapping. Attributes names ${1} doesn't exist. Please fix validator implementation or don't use it.", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "753", enumValueName = "ERROR_BROKEN_VALIDATOR_CONSTRUCTOR_PARAMETER_MAPPING", message = "The configured validator ${0} seems to have a broken annotation attribute to validator constructor parameter mapping. Please fix validator implementation or don't use it.", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "754", enumValueName = "ERROR_BROKEN_VALIDATOR_MISSING_NOARG_CONSTRUCTOR", message = "The configured validator ${0} doesn't have a noarg constructor and therefore seems to be broken. Please fix validator implementation or don't use it.", processorClass = FluentApiProcessor.class)

    public boolean validate() {
        // must check if parameter types are assignable
        FluentApiValidatorWrapper validatorMetaAnnotation = getValidatorAnnotation();
        TypeMirrorWrapper validatorImplType = validatorMetaAnnotation.valueAsTypeMirrorWrapper();
        String[] attributeNamesToConstructorParameterMapping = validatorMetaAnnotation.attributeNamesToConstructorParameterMapping();

        if (attributeNamesToConstructorParameterMapping.length > 0) {

            // First check if annotation attribute Names are correct
            String[] invalidNames = Arrays.stream(attributeNamesToConstructorParameterMapping).filter(e -> !this.validatorAnnotation.getAttribute(e).isPresent()).toArray(String[]::new);
            if (invalidNames.length > 0) {
                this.annotatedElement.compilerMessage(this.validatorAnnotation.unwrap()).asError().write(FluentApiProcessorCompilerMessages.ERROR_BROKEN_VALIDATOR_ATTRIBUTE_NAME_MISMATCH, this.validatorAnnotation.asElement().getSimpleName(), invalidNames);
            }


            // loop over constructors and find if one is matching
            outer:
            for (ExecutableElementWrapper constructor : validatorImplType.getTypeElement().get().getConstructors(Modifier.PUBLIC)) {

                if (constructor.getParameters().size() != attributeNamesToConstructorParameterMapping.length) {
                    continue;
                }

                int i = 0;
                for (String attributeName : attributeNamesToConstructorParameterMapping) {

                    TypeMirrorWrapper attribute = this.validatorAnnotation.getAttributeTypeMirror(attributeName).get();

                    if (!attribute.isAssignableTo(constructor.getParameters().get(i).asType())) {
                        continue outer;
                    }

                    // next
                    i = i + 1;
                }

                // if this is reached, the we have found a matching constructor
                return true;
            }

            annotatedElement.compilerMessage(validatorAnnotation.unwrap()).asError().write(FluentApiProcessorCompilerMessages.ERROR_BROKEN_VALIDATOR_MISSING_NOARG_CONSTRUCTOR, validatorImplType.getSimpleName());
            return false;
        } else {
            // must have a noarg constructor or just the default
            TypeElementWrapper validatorImplTypeElement = validatorImplType.getTypeElement().get();
            boolean hasNoargConstructor = validatorImplTypeElement.filterEnclosedElements().applyFilter(AptkCoreMatchers.IS_CONSTRUCTOR).applyFilter(AptkCoreMatchers.HAS_NO_PARAMETERS).getResult().size() == 1;
            boolean hasJustDefaultConstructor = validatorImplTypeElement.filterEnclosedElements().applyFilter(AptkCoreMatchers.IS_CONSTRUCTOR).hasSize(0);

            if (!(hasNoargConstructor || hasJustDefaultConstructor)) {
                annotatedElement.compilerMessage(validatorAnnotation.unwrap()).asError().write(FluentApiProcessorCompilerMessages.ERROR_BROKEN_VALIDATOR_MISSING_NOARG_CONSTRUCTOR, validatorImplTypeElement.getSimpleName());
            }
        }

        return true;
    }


    public String validatorExpression() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("new ").append(getValidatorAnnotation().valueAsFqn()).append("(");

        boolean isFirst = true;
        for (String attributeName : getValidatorAnnotation().attributeNamesToConstructorParameterMapping()) {

            // add separator
            if (!isFirst) {
                stringBuilder.append(", ");
            } else {
                isFirst = false;
            }

            stringBuilder.append(getValidatorExpressionAttributeValueStringRepresentation(validatorAnnotation.getAttributeWithDefault(attributeName), validatorAnnotation.getAttributeTypeMirror(attributeName).get()));

        }

        stringBuilder.append(")");
        return stringBuilder.toString();
    }


    String getValidatorExpressionAttributeValueStringRepresentation(AnnotationValueWrapper annotationValueWrapper, TypeMirrorWrapper annotationAttributeTypeMirror) {

        if (annotationValueWrapper.isArray()) {
            return annotationValueWrapper.getArrayValue().stream().map(e -> getValidatorExpressionAttributeValueStringRepresentation(e, annotationAttributeTypeMirror.getWrappedComponentType())).collect(Collectors.joining(", ", "new " + annotationAttributeTypeMirror.getWrappedComponentType().getQualifiedName() + "[]{", "}"));
        } else if (annotationValueWrapper.isString()) {
            return "\"" + annotationValueWrapper.getStringValue() + "\"";
        } else if (annotationValueWrapper.isClass()) {
            return annotationValueWrapper.getClassValue().getQualifiedName() + ".class";
        } else if (annotationValueWrapper.isInteger()) {
            return annotationValueWrapper.getIntegerValue().toString();
        } else if (annotationValueWrapper.isLong()) {
            return annotationValueWrapper.getLongValue() + "L";
        } else if (annotationValueWrapper.isBoolean()) {
            return annotationValueWrapper.getBooleanValue().toString();
        } else if (annotationValueWrapper.isFloat()) {
            return annotationValueWrapper.getFloatValue() + "f";
        } else if (annotationValueWrapper.isDouble()) {
            return annotationValueWrapper.getDoubleValue().toString();
        } else if (annotationValueWrapper.isEnum()) {
            return TypeElementWrapper.toTypeElement(annotationValueWrapper.getEnumValue().getEnclosingElement().get()).getQualifiedName() + "." + annotationValueWrapper.getEnumValue().getSimpleName();
        } else {
            throw new IllegalStateException("Got unsupported annotation attribute type : USUALLY THIS CANNOT HAPPEN.");
        }

    }

    public String getValidatorSummary() {
        return validatorAnnotation.getStringRepresentation();
    }

}
