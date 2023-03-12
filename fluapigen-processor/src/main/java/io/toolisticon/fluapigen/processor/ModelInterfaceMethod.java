package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModelInterfaceMethod implements FetchImports {

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

    public String getMethodSignature() {
        return methodSignature;
    }

    public String getMethodCall() {
        return this.executableElement.getSimpleName().toString() + "("
                + String.join(", ", this.executableElement.getParameters().stream().map(e -> {
            return e.getSimpleName().toString();
        }).collect(Collectors.toList())) + ")";
    }


    public ModelInterface getNextModelInterface() {
        return RenderStateHelper.getInterfaceModelForInterfaceSimpleClassName(this.executableElement.getReturnType().getTypeElement().get().getSimpleName());
    }

    public boolean getHasSameTargetBackingBean() {
        return backingBeanModel.getClassName().equals(getNextModelInterface().getBackingBeanModel().getClassName());
    }

    public boolean isParentCall() {
        return !getHasSameTargetBackingBean() && executableElement.hasAnnotation(FluentApiBackingBeanMapping.class);
    }

    public boolean isCreatingChildConfigCall() {
        return !getHasSameTargetBackingBean() && !executableElement.hasAnnotation(FluentApiBackingBeanMapping.class);
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
                .map(e -> new ModelInterfaceImplicitValue(backingBeanModel.getFieldById(e.id()).get(), e))
                .collect(Collectors.toList());
    }

    public List<ModelInterfaceImplicitValue> getImplicitValuesBoundToNextBB() {
        return FluentApiImplicitValueWrapper.wrap(executableElement.unwrap()).stream()
                .filter(e -> e.target() == TargetBackingBean.NEXT)
                .map(e -> new ModelInterfaceImplicitValue(getNextBackingBean().getFieldById(e.id()).get(), e))
                .collect(Collectors.toList());
    }

    public ModelBackingBeanField getParentsBackingBeanField() {
        return getNextBackingBean().getFieldById(executableElement.getAnnotation(FluentApiBackingBeanMapping.class).get().value()).get();
    }

    public String getParentsBackingBeanFieldCollectionImplType() {
        switch (getParentsBackingBeanField().getFieldType().getSimpleName()) {
            case "List": {
                return "java.util.ArrayList";
            }
            case "Set": {
                return "java.util.HashSet";
            }

        }
        return null;
    }

    public boolean validate() {

        // check parameters
        // -> names must match an existing target field

        for (ModelInterfaceMethodParameter parameter : parameters) {
            parameter.validate();
        }

        // -> implicit values must be valid and convertable to target type


        // check return value -> must be Api Interface

        return true;
    }

    public ModelBackingBean getNextBackingBean() {
        ModelBackingBean nextBackingBean;
        // Must handle 2 cases
        // 1. same BB
        if (getHasSameTargetBackingBean()) {
            nextBackingBean = this.backingBeanModel;
        } else {

            // 2. Child Or Parent
            nextBackingBean = getNextModelInterface().getBackingBeanModel();
        }

        return nextBackingBean;
    }

    @Override
    public Set<String> fetchImports() {

        Set<String> imports = new HashSet<>();

        imports.addAll(executableElement.getImports());

        return imports;

    }
}
