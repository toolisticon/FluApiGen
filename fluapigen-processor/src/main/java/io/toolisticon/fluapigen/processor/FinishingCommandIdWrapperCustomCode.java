package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.corematcher.CoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FinishingCommand;
import io.toolisticon.fluapigen.api.FinishingCommandId;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

public class FinishingCommandIdWrapperCustomCode {

    @CustomCodeMethod(FinishingCommandId.class)
    public static boolean validate(FinishingCommandIdWrapper wrapper) {

        return FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(CoreMatchers.IS_METHOD)
                .applyValidator(CoreMatchers.BY_MODIFIER).hasAllOf(Modifier.STATIC)
                .applyValidator(CoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                .validateAndIssueMessages();

    }

    @CustomCodeMethod(FinishingCommandId.class)
    public static String getMethodName(FinishingCommandIdWrapper wrapper) {

        return ((ExecutableElement) wrapper._annotatedElement()).getSimpleName().toString();

    }

    @CustomCodeMethod(FinishingCommand.class)
    public static String getReturnTypeDeclaration(FinishingCommandWrapper wrapper) {
        return TypeMirrorWrapper.wrap(((ExecutableElement) wrapper._annotatedElement()).getReturnType()).getTypeDeclaration();
    }

}
