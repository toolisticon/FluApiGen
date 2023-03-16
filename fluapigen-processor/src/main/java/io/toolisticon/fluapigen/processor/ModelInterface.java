package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.corematcher.AptkCoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelInterface implements FetchImports, Validatable {

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
                commands.add(new ModelInterfaceCommand(executableElement, new ModelInterfaceMethod(executableElement, backingBeanModel)));
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

    @DeclareCompilerMessage(code = "432", enumValueName = "BB_MAPPING_ANNOTATION_MUST_BE_PRESENT_ADD_TO_PARENT_TRAVERSALS", message = "The method is a parent traversal. You must add the ${0} annotation to bind the backing bean to a parent backing bean field.", processorClass = FluentApiProcessor.class)

    @Override
    public boolean validate() {
        boolean outcome = true;

        for (ModelInterfaceMethod method : methods) {
            outcome = outcome & method.validate();

            if (!method.getHasSameTargetBackingBean()
                    && getBackingBeanModel().hasParent()
                    && getBackingBeanModel().getParent().equals(method.getNextBackingBean())){

                    // check if annotation is present
                    if ( !method.getBBMappingAnnotation().isPresent()) {
                        method.getExecutableElement().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT_ADD_TO_PARENT_TRAVERSALS, FluentApiBackingBeanMapping.class.getSimpleName());
                        outcome = false;
                    } else {

                        try {
                            // must check if field can be mapped
                            method.getParentsBackingBeanField();
                        } catch (BBFieldNotFoundException e) {
                            e.writeErrorCompilerMessage();
                            outcome = false;
                        }

                    }
            }
        }

        for (ModelInterfaceCommand command : commands) {
            outcome = outcome & command.validate();
        }

        return outcome;
    }
}
