package io.toolisticon.fluapigen.processor;


import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.command.CommandWithReturnType;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.List;

public class CustomFluentApiWrapperCode {

    /**
     * Validates correct usage of the annotation.
     * <p>
     * - Annotation must be placed on class
     *
     * @param wrapper The wrapper instance
     * @return true if validation was successful, otherwise false.
     */
    @CustomCodeMethod(FluentApi.class)
    static boolean validate(FluentApiWrapper wrapper) {

        // check if annotation is placed on class
        return FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(AptkCoreMatchers.IS_CLASS)
                .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                .validateAndIssueMessages();

    }

    private boolean checkClassName(FluentApiWrapper wrapper){
        if (wrapper.value().isEmpty()) {
            MessagerUtils.error(wrapper._annotatedElement(), FluentApiProcessorMessages2.ERROR_FLUENTAPI_CLASSNAME_MUST_NOT_BE_EMPTY);
            return false;
        }
        if (!wrapper.value().matches("[A-Z_][a-zA-Z_0-9]*")) {
            MessagerUtils.error(wrapper._annotatedElement(), FluentApiProcessorMessages2.ERROR_FLUENTAPI_CLASSNAME_MUST_BE_VALID);
            return false;
        }
        return true;
    }

    @CustomCodeMethod(FluentApi.class)
    static FluentApiState getState(FluentApiWrapper wrapper) {

        List<FluentApiInterfaceWrapper> fluentApiInterfaceWrapperList = FluentElementFilter.createFluentElementFilter((List<Element>)wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(AptkCoreMatchers.BY_ANNOTATION).filterByAllOf(FluentApiInterface.class)
                .<FluentApiInterfaceWrapper>executeCommand(new CommandWithReturnType< Element, FluentApiInterfaceWrapper>() {
                    @Override
                    public FluentApiInterfaceWrapper execute(Element element) {
                        return FluentApiInterfaceWrapper.wrap(element);
                    }
                });

        List<FluentApiBackingBeanWrapper> fluentApiBackingBeanWrapperList = FluentElementFilter.createFluentElementFilter((List<Element>)wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(AptkCoreMatchers.BY_ANNOTATION).filterByAllOf(FluentApiBackingBean.class)
                .<FluentApiBackingBeanWrapper>executeCommand(new CommandWithReturnType< Element, FluentApiBackingBeanWrapper>() {
                    @Override
                    public FluentApiBackingBeanWrapper execute(Element element) {
                        return FluentApiBackingBeanWrapper.wrap(element);
                    }
                });

        List<FluentApiCommandWrapper> fluentApiCommandWrapperList = FluentElementFilter.createFluentElementFilter((List<Element>)wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(AptkCoreMatchers.BY_ANNOTATION).filterByAllOf(FluentApiCommand.class)
                .<FluentApiCommandWrapper>executeCommand(new CommandWithReturnType< Element, FluentApiCommandWrapper>() {
                    @Override
                    public FluentApiCommandWrapper execute(Element element) {
                        return FluentApiCommandWrapper.wrap(element);
                    }
                });

        return new FluentApiState(wrapper, fluentApiInterfaceWrapperList, fluentApiBackingBeanWrapperList, fluentApiCommandWrapperList);


    }





}
