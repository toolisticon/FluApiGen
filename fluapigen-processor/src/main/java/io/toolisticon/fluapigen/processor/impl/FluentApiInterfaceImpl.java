package io.toolisticon.fluapigen.processor.impl;

import io.toolisticon.aptk.tools.ElementUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.corematcher.CoreMatchers;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.FluentApiInterface;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluentApiInterfaceImpl {

    final List<FluentApiInterfaceMethod> methods = new ArrayList<>();

    private final TypeElement element;

    public FluentApiInterfaceImpl(TypeElement element) {
        this.element = element;
        validate();

        // get methods
        List<ExecutableElement> methodElements = ElementUtils.AccessEnclosedElements.<ExecutableElement>getEnclosedElementsOfKind(element, ElementKind.METHOD);
        for (ExecutableElement methodElement : methodElements) {
            methods.add(new FluentApiInterfaceMethod(methodElement));
        }
    }

    public boolean validate() {

        // Must be an interface
        if (!FluentElementValidator.createFluentElementValidator(element)
                .is(CoreMatchers.IS_INTERFACE)
                .applyValidator(CoreMatchers.BY_ANNOTATION).hasAllOf(FluentApiInterface.class)
                .validateAndIssueMessages()) {
            return false;
        }

        return true;
    }

    public List<FluentApiInterfaceMethod> getMethods() {
        return methods;
    }

    public String getInterfaceName() {
        return element.getSimpleName().toString();
    }

    public String getClassName() {
        return element.getSimpleName().toString() + "Impl";
    }

    public Set<String> getImports() {
        Set<String> importSet = new HashSet<>();
        importSet.add(element.getQualifiedName().toString());

        // Add imports of methods
        for (FluentApiInterfaceMethod method : methods) {
            importSet.addAll(method.getImports());
        }

        return importSet;
    }

    public Set<TypeMirrorWrapper> getReferencedInterface() {
        Set<TypeMirrorWrapper> referencedInterfaces = new HashSet<>();

        for (FluentApiInterfaceMethod method : methods) {
            TypeMirrorWrapper returnType = method.getReturnType();

            // Sort out void return types => commands
            if (returnType.unwrap().getKind() != TypeKind.VOID) {
                referencedInterfaces.add(returnType);
            }
        }

        return referencedInterfaces;
    }
}
