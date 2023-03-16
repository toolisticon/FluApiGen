package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.MessagerUtils;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;

@DeclareCompilerMessage(code = "600", enumValueName = "ERROR_CANNOT_FIND_BACKING_BEAN_FIELD", message = "Cannot find field with id '${0}' in backing bean '${1}'", processorClass = FluentApiProcessor.class)
public class BBFieldNotFoundException extends RuntimeException {
    final String bbFieldName;

    final String bbFieldType;

    final AnnotationValue annotationValue;

    final AnnotationMirror annotationMirror;

    final Element element;

    public BBFieldNotFoundException(String bbFieldName, String bbFieldType, Element element, AnnotationMirror annotationMirror, AnnotationValue annotationValue) {
        this.bbFieldName = bbFieldName;
        this.bbFieldType = bbFieldType;
        this.annotationValue = annotationValue;
        this.annotationMirror = annotationMirror;
        this.element = element;
    }

    public BBFieldNotFoundException(String bbFieldName, String bbFieldType, FluentApiImplicitValueWrapper annotation) {
        this(bbFieldName, bbFieldType, annotation._annotatedElement(), annotation._annotationMirror(), annotation.idAsAnnotationValue());
    }

    public BBFieldNotFoundException(String bbFieldName, String bbFieldType, FluentApiBackingBeanMappingWrapper annotation) {
        this(bbFieldName, bbFieldType, annotation._annotatedElement(), annotation._annotationMirror(), annotation.valueAsAnnotationValue());
    }

    public void writeErrorCompilerMessage() {
        MessagerUtils.error(element, annotationMirror, annotationValue, FluentApiProcessorCompilerMessages.ERROR_CANNOT_FIND_BACKING_BEAN_FIELD, bbFieldName, bbFieldType);
    }

}
