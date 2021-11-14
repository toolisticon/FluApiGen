package io.toolisticon.fluapigen.processor.impl;

import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.fluapigen.processor.FinishingCommandWrapper;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluentApiInterfaceMethod {

    private final List<FluentApiInterfaceMethodParameter> parameters = new ArrayList<>();
    private final ExecutableElement element;

    public FluentApiInterfaceMethod(ExecutableElement element) {

        FluentApiInterfaceMethod.verify(element);

        this.element = element;

        // add parameters
        for (VariableElement parameterElement : element.getParameters()) {
            parameters.add(new FluentApiInterfaceMethodParameter(parameterElement));
        }
    }

    public static boolean verify(ExecutableElement element) {

        // must check different scenarios

        return true;
    }

    public TypeMirrorWrapper getReturnType() {
        return TypeMirrorWrapper.wrap(element.getReturnType());
    }

    public String getName() {
        return this.element.getSimpleName().toString();
    }

    public String getParameterNames() {

        StringBuilder stringBuilder = new StringBuilder();

        boolean first = true;
        for (FluentApiInterfaceMethodParameter parameter : parameters) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(parameter.getParameterName());
        }

        return stringBuilder.toString();
    }

    public String getParameterDeclarationString() {

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < parameters.size(); i++) {

            if (i > 0) {
                stringBuilder.append(", ");
            }


            if (element.isVarArgs() && i == parameters.size() - 1) {
                String typeDeclaration = parameters.get(i).getParameterTypeMirror().getTypeDeclaration();
                stringBuilder
                        .append(typeDeclaration.substring(0, typeDeclaration.length() - 2))
                        .append("... ")
                        .append(parameters.get(i).getParameterName());
            } else {
                stringBuilder
                        .append(parameters.get(i).getParameterTypeMirror().getTypeDeclaration())
                        .append(" ")
                        .append(parameters.get(i).getParameterName());
            }

        }

        return stringBuilder.toString();
    }

    public Set<String> getImports() {
        Set<String> importSet = new HashSet<>();

        // add all Imports that are return type related
        importSet.addAll(getReturnType().getImports());

        // add all Imports that are parameter related
        for (FluentApiInterfaceMethodParameter parameter : parameters) {
            importSet.addAll(parameter.getImports());
        }

        return importSet;
    }

    /**
     * Get finishing command.
     *
     * @return the wrapped finishing command
     */
    public FinishingCommandWrapper getFinishingCommand() {
        return isFinishingCommand() ? FinishingCommandWrapper.wrap(element) : null;
    }

    /**
     * Checks if method is finishing command.
     *
     * @return returns true if method is annotated as finishing command, otherwise false
     */
    public boolean isFinishingCommand() {
        return FinishingCommandWrapper.isAnnotated(element);
    }

}
