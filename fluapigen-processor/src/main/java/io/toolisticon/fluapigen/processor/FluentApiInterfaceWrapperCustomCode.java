package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.corematcher.CoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.processor.impl.FluentApiInterfaceMethod;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluentApiInterfaceWrapperCustomCode {

    @CustomCodeMethod(FluentApiInterface.class)
    public static boolean validate(FluentApiInterfaceWrapper wrapper) {

        // Must be an interface
        if (!FluentElementValidator.createFluentElementValidator(wrapper._annotatedElement())
                .is(CoreMatchers.IS_INTERFACE)
                .applyValidator(CoreMatchers.BY_ANNOTATION).hasAllOf(FluentApiInterface.class)
                .validateAndIssueMessages()) {
            return false;
        }

        return true;
    }

    @CustomCodeMethod(FluentApiInterface.class)
    public static List<FluentApiInterfaceMethod> getMethods(FluentApiInterfaceWrapper wrapper) {
        List<FluentApiInterfaceMethod> methods = new ArrayList<>();

        List<ExecutableElement> methodElements = ElementUtils.AccessEnclosedElements.<ExecutableElement>getEnclosedElementsOfKind(wrapper._annotatedElement(), ElementKind.METHOD);
        for (ExecutableElement methodElement : methodElements) {
            methods.add(new FluentApiInterfaceMethod(methodElement));
        }

        return methods;
    }

    @CustomCodeMethod(FluentApiInterface.class)
    public static String getInterfaceName(FluentApiInterfaceWrapper wrapper) {
        return wrapper._annotatedElement().getSimpleName().toString();
    }

    @CustomCodeMethod(FluentApiInterface.class)
    public static String getClassName(FluentApiInterfaceWrapper wrapper) {
        return wrapper._annotatedElement().getSimpleName().toString() + "Impl";
    }

    @CustomCodeMethod(FluentApiInterface.class)
    public static Set<String> getImports(FluentApiInterfaceWrapper wrapper) {
        Set<String> importSet = new HashSet<>();
        importSet.add(((TypeElement) wrapper._annotatedElement()).getQualifiedName().toString());

        // Add imports of methods
        for (FluentApiInterfaceMethod method : getMethods(wrapper)) {
            importSet.addAll(method.getImports());
        }

        return importSet;
    }

    @CustomCodeMethod(FluentApiInterface.class)
    public static Set<TypeMirrorWrapper> getReferencedInterface(FluentApiInterfaceWrapper wrapper) {
        Set<TypeMirrorWrapper> referencedInterfaces = new HashSet<>();

        for (FluentApiInterfaceMethod method : getMethods(wrapper)) {
            TypeMirrorWrapper returnType = method.getReturnType();

            // Sort out void return types => commands
            if (returnType.unwrap().getKind() != TypeKind.VOID) {
                referencedInterfaces.add(returnType);
            }
        }

        return referencedInterfaces;
    }


}
