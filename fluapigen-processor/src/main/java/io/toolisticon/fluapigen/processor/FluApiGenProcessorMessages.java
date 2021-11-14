package io.toolisticon.fluapigen.processor;


import io.toolisticon.aptk.tools.corematcher.ValidationMessage;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiInterface;

/**
 * Messages used by annotation processors.
 */
public enum FluApiGenProcessorMessages implements ValidationMessage {

    ERROR_INTERFACE_MUST_BE_BOUND_WITH_ONE_BACKING_BEAN("FluApiGen_ERROR_002", "The interface '${0}' is associated with multiple backing beans : ${1} and ${2}"),
    ERROR_INVALID_ROOT_INTERFACE("FluApiGen_ERROR_004", "Configured root interface '${0}' must be inner interface of class annotated with " + FluentApi.class.getSimpleName() + " annotation and annotated with " + FluentApiInterface.class.getSimpleName() + "."),
    ERROR_INTERFACE_METHODS_RETURN_TYPE_MUST_BE_BOUND_WITH_A_BACKING_BEAN("FluApiGen_ERROR_003", "The interface '${0}' methods return type '${1}' is not bound as an associated interface with a backing bean");


    /**
     * the message code.
     */
    private final String code;
    /**
     * the message text.
     */
    private final String message;

    /**
     * Constructor.
     *
     * @param code    the message code
     * @param message the message text
     */
    FluApiGenProcessorMessages(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets the code of the message.
     *
     * @return the message code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Gets the message text.
     *
     * @return the message text
     */
    public String getMessage() {
        return message;
    }


}
