package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.InterfaceUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.ElementWrapper;
import io.toolisticon.aptk.tools.wrapper.VariableElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiConverter;
import io.toolisticon.fluapigen.api.TargetBackingBean;
import io.toolisticon.fluapigen.validation.api.FluentApiValidator;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelInterfaceMethodParameter {

    final VariableElementWrapper parameterElement;

    private final ModelBackingBean backingBeanModel;

    private final ModelInterfaceMethod modelInterfaceMethod;

    final String parameterName;

    final TypeMirrorWrapper type;

    final FluentApiBackingBeanMappingWrapper fluentApiBackingBeanMapping;

    ModelInterfaceMethodParameter(VariableElementWrapper variableElement, ModelBackingBean backingBeanModel, ModelInterfaceMethod modelInterfaceMethod) {
        parameterElement = variableElement;
        this.backingBeanModel = backingBeanModel;
        this.modelInterfaceMethod = modelInterfaceMethod;
        parameterName = variableElement.getSimpleName();
        fluentApiBackingBeanMapping = FluentApiBackingBeanMappingWrapper.wrap(variableElement.unwrap());
        type = variableElement.asType();
    }


    public Optional<ModelBackingBeanField> getBackingBeanField() {

        Optional<ModelBackingBeanField> returnValue = Optional.empty();

        if (fluentApiBackingBeanMapping != null) {

            switch (fluentApiBackingBeanMapping.target()) {
                case THIS: {
                    returnValue = backingBeanModel.getFieldById(fluentApiBackingBeanMapping.value());
                    break;
                }
                case NEXT: {

                    returnValue = modelInterfaceMethod.getNextBackingBean().get().getFieldById(fluentApiBackingBeanMapping.value());
                    break;
                }
                case INLINE: {
                    if (modelInterfaceMethod.getInlineBackingBean() == null) {
                        System.out.println("BOOM");
                    }

                    returnValue = modelInterfaceMethod.getInlineBackingBean().get().getFieldById(fluentApiBackingBeanMapping.value());
                    break;
                }
            }

        }

        return returnValue;
    }

    public FluentApiBackingBeanMappingWrapper getFluentApiBackingBeanMapping() {
        return fluentApiBackingBeanMapping;
    }

    public boolean hasConverter() {
        return !fluentApiBackingBeanMapping.converterIsDefaultValue();
        //&& !getFluentApiBackingBeanMapping().converterAsFqn().equals(FluentApiConverter.NoConversion.class.getCanonicalName()
    }


    public boolean hasValidators() {
        return getValidators().size() > 0;
    }

    public List<ModelValidator> getValidators() {

        // First get all enclosing elements - order is from element to top level parent
        List<ElementWrapper<Element>> enclosingElements = this.parameterElement.getAllEnclosingElements(true);

        // now map it to ModelValidators
        List<ModelValidator> allValidatorsWithoutOverwrites = enclosingElements.stream()
                .flatMap(
                        e -> e.getAnnotations().stream()
                                .filter(f -> f.asElement().hasAnnotation(FluentApiValidator.class))
                                .map(f -> new ModelValidator(this.parameterElement, f))
                ).collect(Collectors.toList());

        // Must remove overwritten validators - add them one by one from lowest to highest level and check if the element to add is already overruled by thos in list.
        List<ModelValidator> result = new ArrayList();

        for (ModelValidator nextValidator : allValidatorsWithoutOverwrites) {
            if (!nextValidator.isOverruledBy(result)) {
                result.add(nextValidator);
            }
        }

        return result;

    }

    public String getParameterName() {
        return parameterName;
    }

    String addConverterIfNeeded(String parameterName) {
        return !hasConverter() ? parameterName : "new " + getFluentApiBackingBeanMapping().converterAsTypeMirrorWrapper().getQualifiedName() + "().convert(" + parameterName + ")";
    }


    public String getAssignmentString() {

        Optional<ModelBackingBeanField> backBeanField = getBackingBeanField();

        if (backBeanField.isPresent()) {

            // must distinct two cases
            // 1. Collections - May have ADD or SET action
            // 2. Single Value - only set is allowed per default
            // Might also think about handling Arrays like Collections

            if (backBeanField.get().isCollection()) {

                switch (getFluentApiBackingBeanMapping().action()) {

                    case SET: {

                        // validate types
                        TypeMirrorWrapper typeMirrorWrapper = type;
                        if (typeMirrorWrapper.isCollection() || typeMirrorWrapper.isArray()) {
                            typeMirrorWrapper = typeMirrorWrapper.getWrappedComponentType();
                        }
                        if (!hasConverter()) {
                            if (!typeMirrorWrapper.erasure().isAssignableTo(backBeanField.get().getConcreteType().erasure())) {
                                // must throw validation error
                                throw new IncompatibleParameterTypeException(type.getSimpleName(), backBeanField.get().getFieldName(), backBeanField.get().getFieldType().getTypeDeclaration(), FluentApiBackingBeanMappingWrapper.wrap(parameterElement.unwrap()));
                            }
                        } else {
                            validateConverter(backBeanField.get());
                        }

                        // must handle 3 scenarios depending on parameter type:

                        // 1. varargs / array
                        // 2. Collection
                        // 3. single value

                        if (type.isArray()) {
                            return " = new " + backBeanField.get().getCollectionImplType() + "(Arrays.asList(" + addConverterIfNeeded(getParameterName()) + "));";
                        } else if (type.isCollection()) {
                            return " = new " + backBeanField.get().getCollectionImplType() + "(" + addConverterIfNeeded(getParameterName()) + ");";
                        } else {
                            return " = new " + backBeanField.get().getCollectionImplType() + "(Collections.singletonList( " + addConverterIfNeeded(getParameterName()) + " ));";
                        }

                    }
                    case ADD: {
                        // must handle 3 scenarios depending on parameter type:

                        // 1. varargs / array
                        // 2. Collection
                        // 3. single value

                        if (type.isArray()) {
                            return ".addAll(Arrays.asList(" + addConverterIfNeeded(getParameterName()) + "));";
                        } else if (type.isCollection()) {
                            return ".addAll(" + addConverterIfNeeded(getParameterName()) + ");";
                        } else {
                            return ".add(" + addConverterIfNeeded(getParameterName()) + ");";
                        }

                    }
                }
            }
            /*-

            else if (backBeanField.get().isArray()) {
                switch (getFluentApiBackingBeanMapping().action()) {
                    case SET: {
                        return;
                    }
                    case ADD: {
                        return;
                    }
                }
            }*/

            else {

                if (hasConverter()) {
                    validateConverter(backBeanField.get());
                } else {
                    if (!type.erasure().isAssignableTo(backBeanField.get().getFieldType().erasure())) {
                        // must throw validation error
                        throw new IncompatibleParameterTypeException(type.getSimpleName(), backBeanField.get().getFieldName(), backBeanField.get().getFieldType().getTypeDeclaration(), FluentApiBackingBeanMappingWrapper.wrap(parameterElement.unwrap()));
                    }
                }

                // just parameter assignment
                return " = " + addConverterIfNeeded(getParameterName()) + ";";
            }

        }

        return "";
    }

    private void validateConverter(ModelBackingBeanField backBeanField) {
        TypeMirrorWrapper converter = fluentApiBackingBeanMapping.converterAsTypeMirrorWrapper();

        TypeMirrorWrapper sourceType = this.parameterElement.asType();
        TypeMirrorWrapper targetType = backBeanField.getFieldType();

        List<TypeMirrorWrapper> converterTypeArguments = InterfaceUtils.getResolvedTypeArgumentOfSuperTypeOrInterface(converter.getTypeElement().get(), TypeMirrorWrapper.wrap(FluentApiConverter.class));

        if (!sourceType.isAssignableTo(converterTypeArguments.get(0)) || !converterTypeArguments.get(1).isAssignableTo(targetType)) {
            throw new IncompatibleConverterException(fluentApiBackingBeanMapping, sourceType, targetType, converter, converterTypeArguments);
        }
    }


    @DeclareCompilerMessage(code = "001", enumValueName = "BB_MAPPING_ANNOTATION_MUST_BE_PRESENT", message = "${0} annotation must be present on parameter", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "002", enumValueName = "PARAMETER_AND_MAPPED_BB_FIELD_MUST_HAVE_SAME_TYPE", message = "Parameter type (${0}) must match backing bean field type (${1})", processorClass = FluentApiProcessor.class)
    @DeclareCompilerMessage(code = "003", enumValueName = "BB_MAPPING_COULDNT_BE_RESOLVED", message = "Field ${0} doesn't exist in mapped backing bean ${1}. It must be one of: [${2}]", processorClass = FluentApiProcessor.class)
    public boolean validate() {

        // check validators
        for (ModelValidator validator : this.getValidators()) {
            if (!validator.validate()) {
                return false;
            }
        }


        // fluentApiBackingBeanMapping must not be null -> missing annotation
        if (fluentApiBackingBeanMapping == null) {
            parameterElement.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.BB_MAPPING_ANNOTATION_MUST_BE_PRESENT, FluentApiBackingBeanMapping.class.getSimpleName());
            return false;
        }

        if (!getBackingBeanField().isPresent()) {

            Optional<ModelBackingBean> referencedBackingBean = fluentApiBackingBeanMapping.target() == TargetBackingBean.THIS ? Optional.of(backingBeanModel) : modelInterfaceMethod.getNextBackingBean();
            parameterElement.compilerMessage().asError().write(FluentApiProcessorCompilerMessages.BB_MAPPING_COULDNT_BE_RESOLVED, fluentApiBackingBeanMapping.value(), referencedBackingBean.isPresent() ? referencedBackingBean.get().getBackingBeanInterfaceSimpleName() : "<UNRESOLVED>", referencedBackingBean.isPresent() ? referencedBackingBean.get().getFields().stream().map(e -> "\"" + e.getFieldId() + "\"").collect(Collectors.joining(", ")) : "<UNRESOLVED>"

            );
            return false;
        }


        try {
            getAssignmentString();
        } catch (CompilerErrorException e) {
            e.writeErrorCompilerMessage();
            return false;
        }

        return true;
    }

}
