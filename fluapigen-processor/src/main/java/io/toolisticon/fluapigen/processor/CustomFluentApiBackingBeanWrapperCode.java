package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiInterface;

import javax.lang.model.element.Modifier;

public class CustomFluentApiBackingBeanWrapperCode {

    @CustomCodeMethod(FluentApiBackingBean.class)
    static boolean validate(FluentApiBackingBeanWrapper wrapper) {

        // check if annotation is placed on class and if class is inside type
        return FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(AptkCoreMatchers.IS_INTERFACE)
                .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                .validateAndIssueMessages()
                &&
                FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement().getEnclosingElement())
                        .is(AptkCoreMatchers.IS_CLASS)
                        .setCustomMessage("Interfaces annotated with " + FluentApiInterface.class.getName() + " must be static inner classes of Class annoteted with " + FluentApi.class.getName()).applyValidator(AptkCoreMatchers.BY_ANNOTATION).hasAllOf(FluentApi.class)
                        .validateAndIssueMessages()
                ;

    }

}
