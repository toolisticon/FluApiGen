package io.toolisticon.fluapigen.processor;


import io.toolisticon.aptk.tools.corematcher.ValidationMessage;

/**
 * Messages used by annotation processors.
 */
public enum FluentApiProcessorMessages2 implements ValidationMessage {

    // TODO: Replace this by your own error messages
    ERROR_COULD_NOT_CREATE_CLASS("FluentApi_ERROR_001", "Could not create class ${0} : ${1}"),
    ERROR_VALUE_MUST_NOT_BE_EMPTY("FluentApi_ERROR_002", "Value must not be empty"),
    ERROR_FLUENTAPI_CLASSNAME_MUST_NOT_BE_EMPTY("FluentApi_ERROR_003", "FluentApi.className attribute must not be empty."),
    ERROR_FLUENTAPI_CLASSNAME_MUST_BE_VALID("FluentApi_ERROR_004", "FluentApi.className attribute must not be a valid class name.");

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
        FluentApiProcessorMessages2(String code, String message) {
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
