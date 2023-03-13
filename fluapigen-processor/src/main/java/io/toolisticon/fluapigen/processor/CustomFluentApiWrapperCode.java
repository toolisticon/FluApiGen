package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.command.CommandWithReturnType;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.stream.Collectors;

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
        boolean returnValue = FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(AptkCoreMatchers.IS_CLASS)
                .applyValidator(AptkCoreMatchers.BY_MODIFIER).hasNoneOf(Modifier.PRIVATE)
                .validateAndIssueMessages();

        // Check if Classname is not empty and is a valid class name.
        returnValue = returnValue && checkClassName(wrapper);

        // Check if class contains exactly one fluent api interface that is marked as fluent api root
        returnValue = returnValue && checkIfThereIsExactlyOneFluentApiInterfaceThatMarkedAsRoot(wrapper);

        return returnValue;
    }

    @DeclareCompilerMessage(code="010", enumValueName = "ERROR_FLUENTAPI_CLASSNAME_MUST_NOT_BE_EMPTY", message = "FluentApi annotations className attribute must not be empty.", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code="011", enumValueName = "ERROR_FLUENTAPI_CLASSNAME_MUST_BE_VALID", message = "FluentApi annotations className attribute must not be empty.", processorClass = FluentApiProcessor.class)

    static boolean checkClassName(FluentApiWrapper wrapper) {
        if (wrapper.value().isEmpty()) {
            MessagerUtils.error(wrapper._annotatedElement(), FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_CLASSNAME_MUST_NOT_BE_EMPTY);
            return false;
        }
        if (!wrapper.value().matches("[A-Z_][a-zA-Z_0-9]*")) {
            MessagerUtils.error(wrapper._annotatedElement(), FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_CLASSNAME_MUST_BE_VALID);
            return false;
        }



        return true;
    }


    @DeclareCompilerMessage(code="012", enumValueName = "ERROR_FLUENTAPI_NO_ROOT_INTERFACE", message = "Please add the ${0} annotation to one of the fluent interfaces annotated with ${1}.", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code="013", enumValueName = "ERROR_FLUENTAPI_MULTIPLE_ROOT_INTERFACES", message = "There must be only one interface annotated with the ${0} annotation.", processorClass = FluentApiProcessor.class)
    static boolean checkIfThereIsExactlyOneFluentApiInterfaceThatMarkedAsRoot(FluentApiWrapper wrapper){
        // check if there is exactly one root interface
        List<FluentApiInterfaceWrapper> rootInterfaces = getFluentInterfaces(wrapper).stream().filter(e -> e._annotatedElement().getAnnotation(FluentApiRoot.class) != null).collect(Collectors.toList());
        if (rootInterfaces.size() == 0){
            MessagerUtils.error(wrapper._annotatedElement(), FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_NO_ROOT_INTERFACE, FluentApiRoot.class.getName(), FluentApiInterface.class.getName());
            return false;
        } else if (rootInterfaces.size() > 1) {
            for (FluentApiInterfaceWrapper rootInterface : rootInterfaces) {
                rootInterface.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_FLUENTAPI_MULTIPLE_ROOT_INTERFACES, FluentApiRoot.class.getName());
            }
            return false;
        }

        return true;
    }


    @CustomCodeMethod(FluentApi.class)
    static FluentApiState getState(FluentApiWrapper wrapper) {

        List<FluentApiInterfaceWrapper> fluentApiInterfaceWrapperList = getFluentInterfaces(wrapper);

        List<FluentApiBackingBeanWrapper> fluentApiBackingBeanWrapperList = getBackingBeans(wrapper);

        List<FluentApiCommandWrapper> fluentApiCommandWrapperList = getCommands(wrapper);

        return new FluentApiState(wrapper, fluentApiInterfaceWrapperList, fluentApiBackingBeanWrapperList, fluentApiCommandWrapperList);


    }

    static List<FluentApiInterfaceWrapper> getFluentInterfaces (FluentApiWrapper wrapper){
        return FluentElementFilter.createFluentElementFilter((List<Element>) wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(AptkCoreMatchers.BY_ANNOTATION).filterByAllOf(FluentApiInterface.class)
                .<FluentApiInterfaceWrapper>executeCommand(new CommandWithReturnType<Element, FluentApiInterfaceWrapper>() {
                    @Override
                    public FluentApiInterfaceWrapper execute(Element element) {
                        return FluentApiInterfaceWrapper.wrap(element);
                    }
                });
    }

    static List<FluentApiBackingBeanWrapper> getBackingBeans (FluentApiWrapper wrapper){
        return  FluentElementFilter.createFluentElementFilter((List<Element>) wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(AptkCoreMatchers.BY_ANNOTATION).filterByAllOf(FluentApiBackingBean.class)
                .<FluentApiBackingBeanWrapper>executeCommand(new CommandWithReturnType<Element, FluentApiBackingBeanWrapper>() {
                    @Override
                    public FluentApiBackingBeanWrapper execute(Element element) {
                        return FluentApiBackingBeanWrapper.wrap(element);
                    }
                });
    }


    static List<FluentApiCommandWrapper> getCommands (FluentApiWrapper wrapper) {
        return  FluentElementFilter.createFluentElementFilter((List<Element>) wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(AptkCoreMatchers.BY_ANNOTATION).filterByAllOf(FluentApiCommand.class)
                .<FluentApiCommandWrapper>executeCommand(new CommandWithReturnType<Element, FluentApiCommandWrapper>() {
                    @Override
                    public FluentApiCommandWrapper execute(Element element) {
                        return FluentApiCommandWrapper.wrap(element);
                    }
                });
    }


}
