package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.TypeMirrorWrapper;

import javax.lang.model.element.ExecutableElement;
import java.util.StringJoiner;
import java.util.stream.Collectors;

class Utilities {

    /**
     * Hidden constructor.
     */
    private Utilities () {

    }

    /**
     * Gets the method signature String from Executable Element
     * @param executableElement the Method to get the method signature for
     * @return the method signature
     */
    static String getMethodSignature (ExecutableElement executableElement) {

        TypeMirrorWrapper wrappedReturnType = TypeMirrorWrapper.wrap(executableElement.getReturnType());
        StringBuilder builder =  new StringBuilder();
        builder.append(wrappedReturnType.getTypeDeclaration()).append(" ").append(executableElement.getSimpleName()).append("(");

        builder.append(String.join(", ", executableElement.getParameters().stream().map(element -> {
            return TypeMirrorWrapper.wrap(element.asType()).getTypeDeclaration() + " " + element.getSimpleName().toString();
        }).collect(Collectors.toList())));


        builder.append(")");

        return builder.toString();
    }

}
