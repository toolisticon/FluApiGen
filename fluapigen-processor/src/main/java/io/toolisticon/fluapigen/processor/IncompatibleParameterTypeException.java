package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

@DeclareCompilerMessage(code = "602", enumValueName = "ERROR_INCOMPATIBLE_BACKING_BEAN_MAPPING_TYPES", message = "Cannot map parameter of type ${0} to backing bean field '${1}' with type '${2}'", processorClass = FluentApiProcessor.class)
public class IncompatibleParameterTypeException extends CompilerErrorException{

    final String parameterType;

    final String bbFieldName;

    final String bbFieldType;

    final AnnotationValue annotationValue;

    final AnnotationMirror annotationMirror;

    final Element element;

    public IncompatibleParameterTypeException(
            String parameterType,
            String bbFieldName,
            String bbFieldType,
            Element element,
            AnnotationMirror annotationMirror,
            AnnotationValue annotationValue) {

        this.parameterType = parameterType;
        this.bbFieldName = bbFieldName;
        this.bbFieldType = bbFieldType;
        this.annotationValue = annotationValue;
        this.annotationMirror = annotationMirror;
        this.element = element;

    }

    public IncompatibleParameterTypeException(
            String parameterType,
            String bbFieldName,
            String bbFieldType,
            FluentApiBackingBeanMappingWrapper annotation) {

        this(parameterType, bbFieldName, bbFieldType, annotation._annotatedElement(), annotation._annotationMirror(), annotation.valueAsAnnotationValue());

    }


    public void writeErrorCompilerMessage() {
        MessagerUtils.error(element, annotationMirror, annotationValue, FluentApiProcessorCompilerMessages.ERROR_INCOMPATIBLE_BACKING_BEAN_MAPPING_TYPES, parameterType, bbFieldName, bbFieldType);
    }


}
