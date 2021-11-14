package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.corematcher.CoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.BackingBean;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.processor.impl.FluentApiInterfaceImpl;

import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluentApiWrapperCustomCode {

    @CustomCodeMethod(FluentApi.class)
    public static String getFluentApiClassName(FluentApiWrapper wrapper) {
        return wrapper._annotatedElement().getSimpleName().toString();
    }

    @CustomCodeMethod(FluentApi.class)
    public static BackingBeanWrapper[] getBackingBeans(FluentApiWrapper wrapper) {

        List<TypeElement> filterResult = FluentElementFilter.createFluentElementFilter(wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(CoreMatchers.IS_INTERFACE)
                .applyFilter(CoreMatchers.BY_ANNOTATION).filterByAllOf(BackingBean.class)
                .getResult();

        BackingBeanWrapper[] bbWrappers = new BackingBeanWrapper[filterResult.size()];

        int i = 0;
        for (TypeElement typeElement : filterResult) {
            bbWrappers[i] = BackingBeanWrapper.wrap(typeElement);
            i++;
        }

        return bbWrappers;
    }

    @CustomCodeMethod(FluentApi.class)
    public static FluentApiInterfaceImpl[] getFluentApiInterfaces(FluentApiWrapper wrapper) {

        List<TypeElement> filterResult = FluentElementFilter.createFluentElementFilter(wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(CoreMatchers.IS_INTERFACE)
                .applyFilter(CoreMatchers.BY_ANNOTATION).filterByAllOf(FluentApiInterface.class)
                .getResult();

        FluentApiInterfaceImpl[] fluentApiInterfaces = new FluentApiInterfaceImpl[filterResult.size()];

        int i = 0;
        for (TypeElement typeElement : filterResult) {
            fluentApiInterfaces[i] = new FluentApiInterfaceImpl(typeElement);
            i++;
        }

        return fluentApiInterfaces;

    }

    @CustomCodeMethod(FluentApi.class)
    public static FluentApiInterfaceImpl getRootFluentApiInterface(FluentApiWrapper wrapper) {

        return new FluentApiInterfaceImpl(wrapper.rootInterfaceAsTypeMirrorWrapper().getTypeElement());

    }


    @CustomCodeMethod(FluentApi.class)
    public static boolean validate(FluentApiWrapper wrapper) {
        boolean result = true;

        FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(CoreMatchers.IS_CLASS)
                .validateAndIssueMessages();

        // now check configured root interface
        wrapper.getRootFluentApiInterface().validate();

        // check all other fluent interfaces
        for (FluentApiInterfaceImpl fluentApiInterface : wrapper.getFluentApiInterfaces()) {
            fluentApiInterface.validate();
        }

        // check backing beans config
        for (BackingBeanWrapper backingBean : wrapper.getBackingBeans()) {
            backingBean.validate();
        }

        return result;
    }

    @CustomCodeMethod(FluentApi.class)
    public static Set<String> getImports(FluentApiWrapper wrapper) {

        Set<String> imports = new HashSet<>();

        // get imports of backing beans
        for (BackingBeanWrapper backingBeanWrapper : wrapper.getBackingBeans()) {
            imports.addAll(backingBeanWrapper.getImports());
        }

        // get imports of fluent api interfaces
        for (FluentApiInterfaceImpl fluentApiInterface : wrapper.getFluentApiInterfaces()) {
            imports.addAll(fluentApiInterface.getImports());
        }

        return imports;
    }
}
