package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelInterfaceMethod implements FetchImports, Validatable {

    private final ExecutableElementWrapper executableElement;

    private final ModelBackingBean backingBeanModel;

    private final String methodSignature;

    private final List<ModelInterfaceMethodParameter> parameters = new ArrayList<>();

    private final List<FluentApiImplicitValueWrapper> implicitValues;

    ModelInterfaceMethod(ExecutableElementWrapper executableElement, ModelBackingBean backingBeanModel) {
        this.executableElement = executableElement;
        this.backingBeanModel = backingBeanModel;
        this.methodSignature = executableElement.getMethodSignature();

        this.implicitValues = FluentApiImplicitValueWrapper.wrap(executableElement.unwrap());

        for (VariableElementWrapper variableElementWrapper : executableElement.getParameters()) {
            parameters.add(new ModelInterfaceMethodParameter(variableElementWrapper, this.backingBeanModel, this));
        }

    }

    public ExecutableElementWrapper getExecutableElement() {
        return executableElement;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public String getMethodCall() {
        return this.executableElement.getSimpleName() + "("
                + this.executableElement.getParameters().stream().map(e -> {
            return e.getSimpleName();
        }).collect(Collectors.joining(", ")) + ")";
    }


    public ModelInterface getNextModelInterface() {
        // must catch void,primitive types and arrays
        Optional<TypeElementWrapper> interfaceTypeWrapper = this.executableElement.getReturnType().getTypeElement();
        return interfaceTypeWrapper.isPresent() ? RenderStateHelper.getInterfaceModelForInterfaceSimpleClassName(interfaceTypeWrapper.get().getSimpleName()) : null;
    }

    public boolean getHasSameTargetBackingBean() {

        if (this.isCommandMethod()) {
            return true;
        }

        // need to catch wrong configuration - like broken bb mapping
        ModelInterface nextModelInterface = getNextModelInterface();
        return (nextModelInterface == null && isCommandMethod()) || (nextModelInterface != null && backingBeanModel.getClassName().equals(nextModelInterface.getBackingBeanModel().getClassName()));

    }

    public boolean isParentCall() {

        boolean isCommand = executableElement.hasAnnotation(FluentApiCommand.class);
boolean hasDirectParent = (FluentApiParentBackingBeanMappingWrapper.wrap(executableElement.unwrap()).size() > 0)  && RenderStateHelper.getParents(this.backingBeanModel).size() > 0;

        return hasDirectParent && (isCommand || RenderStateHelper.getParents(this.backingBeanModel).contains(getNextModelInterface().getBackingBeanModel()));
    }

    public boolean isCreatingChildConfigCall() {
        return !getHasSameTargetBackingBean() && !executableElement.hasAnnotation(FluentApiBackingBeanMapping.class);
    }

    public boolean isCommandMethod() {
        return this.executableElement.hasAnnotation(FluentApiCommand.class);
    }

    public ModelInterfaceCommand getCommand() {
        return new ModelInterfaceCommand(executableElement);
    }

    public List<ModelInterfaceMethodParameter> getAllParameters() {
        return parameters;
    }

    public List<ModelInterfaceMethodParameter> getParametersBoundToCurrentBB() {
        return parameters.stream().filter(e -> e.getFluentApiBackingBeanMapping().target() == TargetBackingBean.THIS).collect(Collectors.toList());
    }

    public List<ModelInterfaceMethodParameter> getParametersBoundToNextBB() {
        return parameters.stream().filter(e -> e.getFluentApiBackingBeanMapping().target() == TargetBackingBean.NEXT).collect(Collectors.toList());
    }

    public List<ModelInterfaceImplicitValue> getImplicitValuesBoundToCurrentBB() {
        return FluentApiImplicitValueWrapper.wrap(executableElement.unwrap()).stream()
                .filter(e -> e.target() == TargetBackingBean.THIS)
                .map(e -> {
                    Optional<ModelBackingBeanField> bbField = backingBeanModel.getFieldById(e.id());
                    if (bbField.isPresent()) {
                        return new ModelInterfaceImplicitValue(bbField.get(), e);
                    } else {
                        throw new BBFieldNotFoundException(e.id(), backingBeanModel.getBackingBeanInterfaceSimpleName(), e);
                    }
                })
                .collect(Collectors.toList());
    }


    public List<ModelInterfaceImplicitValue> getImplicitValuesBoundToNextBB() {
        return FluentApiImplicitValueWrapper.wrap(executableElement.unwrap()).stream()
                .filter(e -> e.target() == TargetBackingBean.NEXT)
                .map(e -> {
                    Optional<ModelBackingBeanField> bbField = getNextBackingBean().getFieldById(e.id());
                    if (bbField.isPresent()) {
                        return new ModelInterfaceImplicitValue(bbField.get(), e);
                    } else {
                        throw new BBFieldNotFoundException(e.id(), getNextBackingBean().getBackingBeanInterfaceSimpleName(), e);
                    }
                })
                .collect(Collectors.toList());
    }


    public List<ParentStackProcessing> getParentsBackingBeanFields() {

        List<ParentStackProcessing> parentBBFields = new ArrayList<>();

        List<FluentApiParentBackingBeanMappingWrapper> parentMappings = FluentApiParentBackingBeanMappingWrapper.wrap(executableElement.unwrap());

        ModelBackingBean currentBackingBean = getNextBackingBean();


        int bbCounter = 0;
        String currentVariableName = "currentBackingBean";
        for (int i = 0; i < parentMappings.size(); i++) {

            boolean isLast = (i == parentMappings.size() - 1);

            FluentApiParentBackingBeanMappingWrapper parentMapping = parentMappings.get(i);
            String nextVariableName = "backingBean" + i;

            if (currentBackingBean == null) {
                // TODO: throw exception
            }

            String bbFieldName = parentMapping.value();

            Optional<ModelBackingBeanField> field = currentBackingBean.getFieldById(bbFieldName);

            if (!field.isPresent()) {
                throw new BBFieldNotFoundException(bbFieldName, currentBackingBean.interfaceClassName(), parentMapping);
            }

            // prepare next iteration
            parentBBFields.add(new ParentStackProcessing(currentVariableName, nextVariableName, currentBackingBean != null ? currentBackingBean.getClassName() : null, field.get().getFieldName(), i == 0, isLast, field.get().isCollection()));
            currentBackingBean = currentBackingBean.getParent();
            currentVariableName = nextVariableName;


        }

        return parentBBFields;
    }


    public static class ParentStackProcessing {

        final String currentBBVariableName;
        final String nextBBVariableName;

        final String nextBBTypeName;
        final String fieldName;

        final boolean isFirst;

        final boolean isLast;

        final boolean isCollection;

        public ParentStackProcessing(String currentBBVariableName, String nextBBVariableName, String nextBBTypeName, String fieldName, boolean isFirst, boolean isLast, boolean isCollection) {
            this.currentBBVariableName = currentBBVariableName;
            this.nextBBVariableName = nextBBVariableName;
            this.nextBBTypeName = nextBBTypeName;
            this.fieldName = fieldName;
            this.isFirst = isFirst;
            this.isLast = isLast;
            this.isCollection = isCollection;
        }

        public String getCurrentBBVariableName() {
            return currentBBVariableName;
        }

        public String getNextBBVariableName() {
            return nextBBVariableName;
        }

        public String getNextBBTypeName() {
            return nextBBTypeName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public boolean isFirst() {
            return isFirst;
        }

        public boolean isLast() {
            return isLast;
        }

        public boolean isCollection() {
            return isCollection;
        }
    }

    public List<FluentApiParentBackingBeanMappingWrapper> getParentBBMappingAnnotation() {
        return FluentApiParentBackingBeanMappingWrapper.wrap(this.executableElement.unwrap());
    }

    @DeclareCompilerMessage(code = "190", enumValueName = "ERROR_RETURN_TYPE_MUST_BE_FLUENT_INTERFACE", message = "Return type '${0}' must be a fluent interface!", processorClass = FluentApiProcessor.class)
    public boolean validate() {

        boolean outcome = true;
        // check parameters
        // -> names must match an existing target field

        for (ModelInterfaceMethodParameter parameter : parameters) {
            outcome = outcome & parameter.validate();
        }

        try {

            // -> implicit values must be valid and convertable to target type
            for (ModelInterfaceImplicitValue implicitValue : getImplicitValuesBoundToCurrentBB()) {
                outcome = outcome & implicitValue.validate();
            }

            for (ModelInterfaceImplicitValue implicitValue : getImplicitValuesBoundToNextBB()) {
                outcome = outcome & implicitValue.validate();
            }

        } catch (BBFieldNotFoundException e) {
            e.writeErrorCompilerMessage();
            outcome = false;
        }

        // check return value -> must be Api Interface
        if (!executableElement.hasAnnotation(FluentApiCommand.class)) {

            if (RenderStateHelper.getInterfaceModelForInterfaceSimpleClassName(executableElement.getReturnType().getSimpleName()) == null) {
                executableElement.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_RETURN_TYPE_MUST_BE_FLUENT_INTERFACE, executableElement.getReturnType().isVoidType() ? "void" : executableElement.getReturnType().getSimpleName());
                outcome = false;
            }

        }


        return outcome;
    }


    public ModelBackingBean getNextBackingBean() {
        ModelBackingBean nextBackingBean;
        // Must handle 2 cases
        if (isParentCall()) {
            nextBackingBean = this.backingBeanModel.getParent();
        } else if (getHasSameTargetBackingBean()) {
            // 1. same BB
            nextBackingBean = this.backingBeanModel;
        } else  {
            // 2. Child Or Parent
            nextBackingBean = getNextModelInterface().getBackingBeanModel();
        }

        return nextBackingBean;
    }

    @Override
    public Set<String> fetchImports() {

        Set<String> imports = new HashSet<>();

        imports.addAll(executableElement.getImports());

        if (isCommandMethod()) {
            imports.addAll(getCommand().fetchImports());
        }
        return imports;

    }


}
