package io.toolisticon.fluapigen.processor.impl;

import io.toolisticon.aptk.tools.TypeMirrorWrapper;

import javax.lang.model.element.VariableElement;
import java.util.Set;

public class FluentApiInterfaceMethodParameter {

    private final VariableElement element;

    FluentApiInterfaceMethodParameter(VariableElement element) {
        this.element = element;
    }

    public String getParameterName() {
        return element.getSimpleName().toString();
    }

    public TypeMirrorWrapper getParameterTypeMirror() {
        return TypeMirrorWrapper.wrap(element.asType());
    }

    public Set<String> getImports() {
        return getParameterTypeMirror().getImports();
    }

}
