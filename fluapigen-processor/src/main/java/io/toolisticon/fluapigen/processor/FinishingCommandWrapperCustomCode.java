package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.corematcher.CoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.fluapigen.api.FinishingCommand;
import io.toolisticon.fluapigen.api.FinishingCommandId;

import javax.lang.model.element.ExecutableElement;
import java.util.ArrayList;
import java.util.List;

public class FinishingCommandWrapperCustomCode {

    private static List<FinishingCommandIdWrapper> getFinishingCommandMethods(FinishingCommandWrapper wrapper) {
        List<ExecutableElement> methods = FluentElementFilter.createFluentElementFilter(wrapper.valueAsTypeMirrorWrapper().getTypeElement().getEnclosedElements())
                .applyFilter(CoreMatchers.IS_METHOD)
                .applyFilter(CoreMatchers.BY_ANNOTATION).filterByAllOf(FinishingCommandId.class)
                .getResult();

        List<FinishingCommandIdWrapper> result = new ArrayList<>();

        for (ExecutableElement executableElement : methods) {
            result.add(FinishingCommandIdWrapper.wrap(executableElement));
        }

        return result;
    }


    @CustomCodeMethod(FinishingCommand.class)
    public static boolean validate(FinishingCommandWrapper wrapper) {

        // Validate command

        FinishingCommandIdWrapper finishingCommandIdWrapper = getFinishingCommandMethod(wrapper);

        // not found
        if (finishingCommandIdWrapper == null) {

            return false;
        }

        // validate annotated method
        return finishingCommandIdWrapper.validate();

    }



    @CustomCodeMethod(FinishingCommand.class)
    public static FinishingCommandIdWrapper getFinishingCommandMethod(FinishingCommandWrapper wrapper) {

        List<FinishingCommandIdWrapper> finishingCommandMethods = getFinishingCommandMethods(wrapper);

        // Check default value
        if (wrapper.targetIdIsDefaultValue() && finishingCommandMethods.size() == 1) {
            return finishingCommandMethods.get(0);
        }

        // search targetId
        for (FinishingCommandIdWrapper finishingCommandMethod : finishingCommandMethods) {

            if (wrapper.targetId().equals(finishingCommandMethod.value())) {
                return finishingCommandMethod;
            }

        }

        // not found
        return null;
    }


    @CustomCodeMethod(FinishingCommand.class)
    public static String getFinishingCommandCall(FinishingCommandWrapper wrapper) {
        return wrapper.valueAsTypeMirrorWrapper().getSimpleName() + "." + getFinishingCommandMethod(wrapper).getMethodName();
    }




}
