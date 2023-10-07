package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;

import java.util.List;

@DeclareCompilerMessage(code = "603", enumValueName = "BB_MAPPING_INVALID_CONVERTER", message = "The source and or target type of the configured converter ${4} must be <${0},${1}> but are <${2},${3}>", processorClass = FluentApiProcessor.class)

public class IncompatibleConverterException extends CompilerErrorException{

    final FluentApiBackingBeanMappingWrapper annotation;
    final TypeMirrorWrapper sourceType;
    final TypeMirrorWrapper targetType;

    final TypeMirrorWrapper converterType;

    final List<TypeMirrorWrapper> converterTypeArguments;

    public IncompatibleConverterException(FluentApiBackingBeanMappingWrapper annotation,TypeMirrorWrapper sourceType, TypeMirrorWrapper targetType, TypeMirrorWrapper converterType, List<TypeMirrorWrapper> converterTypeArguments) {
        this.annotation = annotation;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.converterType = converterType;
        this.converterTypeArguments = converterTypeArguments;
    }

    @Override
    public void writeErrorCompilerMessage() {
        annotation.converterAsAttributeWrapper().compilerMessage().asError().write(FluentApiProcessorCompilerMessages.BB_MAPPING_INVALID_CONVERTER,converterTypeArguments.get(0),converterTypeArguments.get(1), sourceType, targetType, converterType.getSimpleName());
    }
}
