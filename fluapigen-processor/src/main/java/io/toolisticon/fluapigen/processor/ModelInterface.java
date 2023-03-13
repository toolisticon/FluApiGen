package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelInterface implements FetchImports {

    private final FluentApiInterfaceWrapper wrapper;

    private final ModelBackingBean backingBeanModel;

    private final List<ModelInterfaceMethod> methods = new ArrayList<>();

    private final List<ModelInterfaceCommand> commands = new ArrayList<>();

    ModelInterface(FluentApiInterfaceWrapper wrapper, ModelBackingBean backingBeanModel) {
        this.wrapper = wrapper;
        this.backingBeanModel = backingBeanModel;

        // get Methods
        for (ExecutableElementWrapper executableElement : FluentElementFilter.createFluentElementFilter(this.wrapper._annotatedElement().getEnclosedElements()).applyFilter(AptkCoreMatchers.IS_METHOD).getResult().stream().map(ExecutableElementWrapper::wrap).collect(Collectors.toList())) {
            if (!executableElement.getAnnotation(FluentApiCommand.class).isPresent()) {
                methods.add(new ModelInterfaceMethod(executableElement, backingBeanModel));
            } else {
                commands.add(new ModelInterfaceCommand(executableElement));
            }
        }

        // add to render state
        RenderStateHelper.addInterfaceModel(this);
    }

    public ModelBackingBean getBackingBeanModel() {
        return backingBeanModel;
    }

    public boolean isRootInterface() {
        return wrapper._annotatedElement().getAnnotation(FluentApiRoot.class) != null;
    }

    public String getClassName() {
        return wrapper._annotatedElement().getSimpleName().toString() + "Impl";
    }

    public String interfaceClassSimpleName() {
        return wrapper._annotatedElement().getSimpleName().toString();
    }

    public String interfaceClassName() {
        return wrapper._annotatedElement().getEnclosingElement().getSimpleName().toString() + "." + wrapper._annotatedElement().getSimpleName().toString();
    }

    public List<ModelInterfaceMethod> getMethods() {
        return methods;
    }

    public List<ModelInterfaceCommand> getCommands() {
        return commands;
    }


    @Override
    public Set<String> fetchImports() {
        Set<String> imports = new HashSet<>();

        for (FetchImports modelInterfaceMethod : this.methods) {
            imports.addAll(modelInterfaceMethod.fetchImports());
        }

        for (FetchImports modelInterfaceMethod : this.commands) {
            imports.addAll(modelInterfaceMethod.fetchImports());
        }

        return imports;
    }
}
