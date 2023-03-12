package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApi;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

class ValidationUtils {

    /**
     * Hidden constructor
     */
    private ValidationUtils() {

    }

    static boolean isDefinedInSameClass(TypeElement referenceElement, TypeElement elementToCheck, boolean issueError) {

        // get Parents of both types
        Element referenceParentElement = referenceElement.getEnclosingElement();
        Element elementToCheckParentElement = elementToCheck.getEnclosingElement();




        return referenceParentElement.equals(elementToCheckParentElement);


    }

    static boolean  isParentElementAnnotatedWithFluentApi(TypeElement elementToCheck) {
        return isParentElementAnnotatedWithFluentApi(elementToCheck, true);
    }

    static boolean isParentElementAnnotatedWithFluentApi(TypeElement elementToCheck, boolean issueError) {
        FluentElementValidator<TypeElement> validator = FluentElementValidator.createFluentElementValidator(elementToCheck.getEnclosingElement())
                .is(AptkCoreMatchers.IS_CLASS)
                .applyValidator(AptkCoreMatchers.BY_ANNOTATION).hasAllOf(FluentApi.class)
                .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE);

        return issueError ? validator.validateAndIssueMessages() : validator.justValidate();

    }


}
