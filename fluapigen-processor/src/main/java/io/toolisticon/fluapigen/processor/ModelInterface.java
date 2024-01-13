package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.InterfaceUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import javax.lang.model.element.TypeElement;
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


        for (ExecutableElementWrapper executableElement : InterfaceUtils.getMethodsToImplement(TypeElementWrapper.wrap((TypeElement) this.wrapper._annotatedElement()))) {

            if (executableElement.isDefault()) {
                // ignore default methods
                continue;
            }
            //}
            // get Methods
            //for (ExecutableElementWrapper executableElement : FluentElementFilter.createFluentElementFilter(this.wrapper._annotatedElement().getEnclosedElements()).applyFilter(AptkCoreMatchers.IS_METHOD).applyFilter(AptkCoreMatchers.BY_MODIFIER).filterByNoneOf(Modifier.DEFAULT).getResult().stream().map(ExecutableElementWrapper::wrap).collect(Collectors.toList())) {
            methods.add(new ModelInterfaceMethod(executableElement, backingBeanModel));
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

    public boolean hasTypeParameter() {
        return TypeMirrorWrapper.wrap(this.wrapper._annotatedElement().asType()).hasTypeArguments();
    }

    public String getTypeParametersString() {

        TypeElementWrapper testInterface = TypeElementWrapper.wrap((TypeElement) wrapper._annotatedElement());

        if (testInterface.getTypeParameters().size() > 0) {

            StringBuilder stringBuilder = new StringBuilder("<");

            stringBuilder.append(testInterface.getTypeParameters().stream().map(
                    e -> e.toString() + (e.getBounds().size() == 1 && e.getBounds().get(0).getQualifiedName().equals(Object.class.getCanonicalName()) ? "" : " extends " + e.getBounds().stream().map(f -> f.getTypeDeclaration()).collect(Collectors.joining(" & ")))
            ).collect(Collectors.joining(", ")));

            stringBuilder.append(">");

            return stringBuilder.toString();
        }
        return "";
    }

    public String getTypeParameterNamesString() {

        TypeElementWrapper testInterface = TypeElementWrapper.wrap((TypeElement) wrapper._annotatedElement());

        if (testInterface.getTypeParameters().size() > 0) {

            StringBuilder stringBuilder = new StringBuilder("<");

            stringBuilder.append(testInterface.getTypeParameters().stream().map(e -> e.toString()).collect(Collectors.joining(", ")));

            stringBuilder.append(">");

            return stringBuilder.toString();
        }
        return "";
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

        TypeElementWrapper typeElementWrapper = TypeElementWrapper.wrap((TypeElement) this.wrapper._annotatedElement());
        //if (typeElementWrapper.getTypeParameters())

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

            if (method.isCommandMethod()) {

            } else if (!method.getHasSameTargetBackingBean()
                    && getBackingBeanModel().hasParent()
                    && (method.getNextBackingBean().isPresent() && getBackingBeanModel().getParent().equals(method.getNextBackingBean().get()))) {

                // check if annotation is present
                if (method.getParentBBMappingAnnotation().size() == 0) {
                    method.getExecutableElement().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT_ADD_TO_PARENT_TRAVERSALS, FluentApiParentBackingBeanMapping.class.getSimpleName());
                    outcome = false;
                } else {

                    try {
                        // must check if field can be mapped
                        method.getParentsBackingBeanFields();
                    } catch (BaseException e) {
                        e.writeErrorCompilerMessage();
                        outcome = false;
                    }

                }
            }
        }


        return outcome;
    }
}
