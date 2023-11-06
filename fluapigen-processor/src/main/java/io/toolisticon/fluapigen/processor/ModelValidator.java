package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.wrapper.AnnotationMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.AnnotationValueWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;

import javax.lang.model.element.ExecutableElement;
import java.util.List;

public class ModelValidator {


    private final AnnotationMirrorWrapper validatorAnnotation;

    public ModelValidator(AnnotationMirrorWrapper validatorAnnotation) {
        this.validatorAnnotation = validatorAnnotation;
    }

    FluentApiValidatorWrapper getValidatorAnnotation() {
        return FluentApiValidatorWrapper.wrap(this.validatorAnnotation.asElement().unwrap());
    }

    // TODO NOT USED YET, MUST BE IMPLEMENTED
    public boolean validate() {
        // must check if parameter types are assignable
        FluentApiValidatorWrapper validatorMetaAnnotation = getValidatorAnnotation();
        TypeMirrorWrapper validatorImplType = validatorMetaAnnotation.valueAsTypeMirrorWrapper();
        String[] parameterNames = validatorMetaAnnotation.parameterNames();

        if (parameterNames.length > 0) {

        } else if (!validatorImplType.getTypeElement().isPresent()) {
            // couldn't get type element
        } else {
            // must have a noarg constructor or just the default
            TypeElementWrapper validatorImplTypeElement = validatorImplType.getTypeElement().get();
            boolean hasNoargConstructor = validatorImplTypeElement.filterEnclosedElements().applyFilter(AptkCoreMatchers.IS_CONSTRUCTOR).applyFilter(AptkCoreMatchers.HAS_NO_PARAMETERS).getResult().size() == 1;
            boolean hasJustDefaultConstructor = validatorImplTypeElement.filterEnclosedElements().applyFilter(AptkCoreMatchers.IS_CONSTRUCTOR).hasSize(0);

            if (! ( hasNoargConstructor || hasJustDefaultConstructor)) {
                //ToolingProvider.getTooling().getMessager().
                //this.validatorAnnotation.
            }
        }

        return true;
    }


    public String validatorExpression() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("new ").append(getValidatorAnnotation().valueAsFqn()).append("(");

        boolean isFirst = true;
        for (String parameterName : getValidatorAnnotation().parameterNames()) {

            // add separator
            if (!isFirst) {
                stringBuilder.append(", ");
            } else {
                isFirst = false;
            }

            AnnotationValueWrapper attribute = validatorAnnotation.getAttributeWithDefault(parameterName);

            if (attribute.isString()) {
                stringBuilder.append("\"").append(attribute.getStringValue()).append("\"");
            } else if (attribute.isClass()) {
                stringBuilder.append(attribute.getClassValue().getQualifiedName()).append(".class");
            } else if (attribute.isInteger()) {
                stringBuilder.append(attribute.getIntegerValue());
            } else if (attribute.isLong()) {
                stringBuilder.append(attribute.getLongValue()).append("L");
            } else if (attribute.isBoolean()) {
                stringBuilder.append(attribute.getBooleanValue());
            } else if (attribute.isFloat()) {
                stringBuilder.append(attribute.getFloatValue()).append("f");
            } else if (attribute.isDouble()) {
                stringBuilder.append(attribute.getFloatValue());
            } else if (attribute.isEnum()) {
                stringBuilder.append(TypeElementWrapper.toTypeElement(attribute.getEnumValue().getEnclosingElement().get()).getQualifiedName());
                stringBuilder.append(".");
                stringBuilder.append(attribute.getEnumValue().getSimpleName());
            }

        }

        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public String getValidatorSummary() {
        return validatorAnnotation.getStringRepresentation();
    }

}
