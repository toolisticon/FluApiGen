package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.compilermessage.api.DeclareCompilerMessage;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.wrapper.ExecutableElementWrapper;
import io.toolisticon.aptk.tools.wrapper.TypeElementWrapper;
import io.toolisticon.fluapigen.api.FluentApiCommand;

/**
 * Model for command type.
 */
public class ModelInterfaceCommandType implements Validatable{

    private final TypeElementWrapper typeElement;

    private final FluentApiCommandWrapper fluentApiCommandWrapper;

    public ModelInterfaceCommandType(TypeElementWrapper typeElement) {
        this.typeElement = typeElement;
        fluentApiCommandWrapper = FluentApiCommandWrapper.wrap(typeElement.unwrap());
    }



    @Override
    @DeclareCompilerMessage(code="230", enumValueName = "WARNING_VALUE_WILL_BE_IGNORED_IF_A_TYPE_IS_ANNOTATED", message = "The value will be ignored if annotation is placed on type.", processorClass = FluentApiProcessor.class)
    public boolean validate() {

        boolean outcome = true;

        // value attribute is ignored if annotation is placed on type
        if (!fluentApiCommandWrapper.valueIsDefaultValue()) {
            fluentApiCommandWrapper.valueAsAttributeWrapper().compilerMessage().asWarning().write(FluentApiProcessorCompilerMessages.WARNING_VALUE_WILL_BE_IGNORED_IF_A_TYPE_IS_ANNOTATED);
        }


        return outcome;

    }
}
