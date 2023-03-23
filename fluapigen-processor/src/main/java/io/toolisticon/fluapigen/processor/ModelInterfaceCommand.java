package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;

import java.util.HashSet;
import java.util.Set;

public class ModelInterfaceCommand implements FetchImports, Validatable {

    private final ExecutableElementWrapper executableElement;
    private final FluentApiCommandWrapper fluentApiCommandWrapper;

    private final String methodSignature;

    ModelInterfaceCommand(ExecutableElementWrapper executableElement) {
        this.executableElement = executableElement;
        this.fluentApiCommandWrapper = FluentApiCommandWrapper.wrap(executableElement.unwrap());
        this.methodSignature = executableElement.getMethodSignature();
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public String getCommandMethod() {
        return this.fluentApiCommandWrapper.valueAsTypeMirrorWrapper().getSimpleName() + "." + fluentApiCommandWrapper.getCommandMethodName();
    }

    public boolean hasReturnType() {
        return !executableElement.getReturnType().isVoidType();
    }



    @Override
    public Set<String> fetchImports() {

        Set<String> imports = new HashSet<>();

        // Method signature imports
        imports.addAll(executableElement.getImports());

        // target command type
        imports.add(this.fluentApiCommandWrapper.valueAsTypeMirrorWrapper().getQualifiedName());

        return imports;

    }

    @Override
    public boolean validate() {

        boolean outcome = true;

        return outcome;

    }
}
