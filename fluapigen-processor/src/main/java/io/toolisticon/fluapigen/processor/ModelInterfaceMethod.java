package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
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


    public ModelBackingBeanField getParentsBackingBeanField() {

        String bbFieldName = executableElement.getAnnotation(FluentApiBackingBeanMapping.class).get().value();
        Optional<ModelBackingBeanField> field = getNextBackingBean().getFieldById(bbFieldName);

        if (!field.isPresent()) {
            throw new BBFieldNotFoundException(bbFieldName,getNextBackingBean().interfaceClassName(), FluentApiBackingBeanMappingWrapper.wrap(executableElement.unwrap()));
        }

        return field.get();
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

    public Optional<FluentApiBackingBeanMapping> getBBMappingAnnotation() {
        return this.executableElement.getAnnotation(FluentApiBackingBeanMapping.class);
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
                executableElement.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.ERROR_RETURN_TYPE_MUST_BE_FLUENT_INTERFACE, executableElement.getReturnType().isVoidType()? "void" : executableElement.getReturnType().getSimpleName());
                outcome = false;
            }

        }


        return outcome;
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
