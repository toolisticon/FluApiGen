package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;

import javax.lang.model.element.Modifier;

public class CustomFluentApiCommandWrapperCode {

    @CustomCodeMethod(FluentApiCommand.class)
    static String getCommandMethodName (FluentApiCommandWrapper wrapper) {
        return wrapper.valueAsTypeMirrorWrapper().getTypeElement().get().filterEnclosedElements()
                .applyFilter(AptkCoreMatchers.IS_METHOD).getResult().get(0).getSimpleName().toString();
    }

    @CustomCodeMethod(FluentApiCommand.class)
    static boolean validate(FluentApiCommandWrapper wrapper) {

        // check if annotation is placed on class and if class is inside type
        boolean result = FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(AptkCoreMatchers.IS_INTERFACE)
                .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                .validateAndIssueMessages()
                &
                FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement().getEnclosingElement())
                        .is(AptkCoreMatchers.IS_CLASS)
                        .setCustomMessage("Interfaces annotated with " + FluentApiInterface.class.getName() + " must be static inner classes of Class annoteted with " + FluentApi.class.getName()).applyValidator(AptkCoreMatchers.BY_ANNOTATION).hasAllOf(FluentApi.class)
                        .validateAndIssueMessages()
                ;

        // check if class exactly contains one static function.
        if(wrapper._annotatedElement().getEnclosedElements().size() != 1) {
            MessagerUtils.error(wrapper._annotatedElement(), "Class must contain exactly one static method, nothing else");
            return false;
        } else {
            result = result & FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement().getEnclosedElements().get(0))
                    .is(AptkCoreMatchers.IS_METHOD)
                    .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasAllOf(Modifier.STATIC)
                    .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                    .applyValidator(AptkCoreMatchers.BY_NUMBER_OF_PARAMETERS).hasOneOf(1)
                    .validateAndIssueMessages();
        }

        // First parameter must be a BackingBean type from same class as annotated element

        return result;

    }


}
