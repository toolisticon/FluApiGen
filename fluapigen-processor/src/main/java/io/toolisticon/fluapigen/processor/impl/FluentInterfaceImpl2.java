package io.toolisticon.fluapigen.processor.impl;

import io.toolisticon.aptk.tools.AnnotationUtils;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.TypeUtils;
import io.toolisticon.aptk.tools.corematcher.CoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FinishingCommandId;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * Class to store interface related data for processing.
 */
public class FluentInterfaceImpl2 {

    static class FluentCommandIdImpl {

        final ExecutableElement method;
        final String targetClass;
        final String targetId;

        public FluentCommandIdImpl(ExecutableElement method) {
            if (!FluentElementValidator.createFluentElementValidator(method)
                    .applyValidator(CoreMatchers.BY_ANNOTATION).hasAllOf(FinishingCommandId.class)
                    .validateAndIssueMessages()) {
                throw new IllegalStateException("Expected passed Executable element to be annotated with FinishingCommandId");
            }
            this.method = method;
            this.targetClass = AnnotationUtils.getClassAttributeFromAnnotationAsFqn(method, FinishingCommandId.class);
            this.targetId = AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(AnnotationUtils.getAnnotationMirror(method, FinishingCommandId.class)).getValue().toString();
        }

        String getReturnTypeAsFqn() {
            return TypeUtils.TypeRetrieval.getTypeElement(targetClass).getQualifiedName().toString();
        }

        List<ExecutableElement> getTargetMethodsOfClass() {
            TypeElement typeElement = TypeUtils.TypeRetrieval.getTypeElement(targetClass);

            return FluentElementFilter.createFluentElementFilter(typeElement.getEnclosedElements())
                    .applyFilter(CoreMatchers.IS_METHOD)
                    .applyFilter(CoreMatchers.BY_ANNOTATION).filterByAllOf(FinishingCommandId.class)
                    .getResult();

        }

        /**
         * Gets the target executable element.
         *
         * @return
         */
        ExecutableElement getTargetMethod() {
            List<ExecutableElement> targetMethods = getTargetMethodsOfClass();
            for (ExecutableElement targetMethod : targetMethods) {
                AnnotationMirror annotationMirror = AnnotationUtils.getAnnotationMirror(targetMethod, FinishingCommandId.class);
                String targetId = AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror).getValue().toString();
                if (this.targetId.equals(targetId)) {
                    return targetMethod;
                }
            }

            // This cannot happen
            return null;
        }

        boolean validateTarget() {

            boolean valid = true;

            ExecutableElement annotatedMethod = getTargetMethod();

            // annotated method must be static, private and have exactly one parameter
            valid = FluentElementValidator.createFluentElementValidator(annotatedMethod)
                    .applyValidator(CoreMatchers.BY_MODIFIER).hasAllOf(Modifier.STATIC)
                    .applyValidator(CoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                    .applyValidator(CoreMatchers.BY_NUMBER_OF_PARAMETERS).hasOneOf(1)
                    .validateAndIssueMessages();

            // target id must be unique in class
            for (ExecutableElement allAnnotatedMethods : getTargetMethodsOfClass()) {

                // must not take care of null values - know annotation is present
                if (targetId.equals(getFluentCommandForMethod(allAnnotatedMethods)) && !annotatedMethod.equals(allAnnotatedMethods)) {
                    AnnotationMirror annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedMethod, FinishingCommandId.class);
                    AnnotationValue annotationValue = AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror);
                    MessagerUtils.error(allAnnotatedMethods, annotationMirror, annotationValue, "FinishigCommandId must be unique in class");
                    valid = false;
                }

            }
            return valid;
        }


    }

    static FluentCommandIdImpl getFluentCommandForMethod(ExecutableElement executableElement) {

        if (FluentElementValidator.createFluentElementValidator(executableElement)
                .applyValidator(CoreMatchers.BY_ANNOTATION).hasAllOf(FinishingCommandId.class)
                .validateAndIssueMessages()) {
            return new FluentCommandIdImpl(executableElement);
        }
        return null;
    }

}
