package io.toolisticon.fluapigen.validation.api;

/**
 * The Validator interface.
 *
 * @param <TYPE> The type which can be validated
 */
public interface Validator<TYPE> {
    /**
     * Validates passed in instance
     *
     * @param obj the object to validate
     * @return true if obj is valid otherwise false
     */
    boolean validate(TYPE obj);

    default boolean validate(TYPE[] obj) {
        if (obj != null) {
            for (TYPE element : obj) {
                if (!validate(element)) {
                    return false;
                }
            }
        }
        return true;
    }
}
