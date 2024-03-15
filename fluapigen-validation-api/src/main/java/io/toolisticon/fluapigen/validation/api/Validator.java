package io.toolisticon.fluapigen.validation.api;

import java.util.Arrays;
import java.util.Collection;

/**
 * The Validator interface.
 *
 * One hint validators should always return true for null values!
 * To prevent null values you should use {@link NotNull} validator in combination.
 *
 * @param <TYPE> The type which can be validated
 */
public interface Validator<TYPE> {
    /**
     * Validates passed in instance.
     *
     * @param object the object to validate
     * @return true if object is valid otherwise false
     */
    boolean validate(TYPE object);
    default boolean validate(TYPE[] array){
        if(array!=null){
            return validate(Arrays.asList(array));
        }
        return true;
    }
    default boolean validate(Iterable<TYPE> iterable) {
        if (iterable != null) {
            for (TYPE element : iterable) {
                if (!validate(element)) {
                    return false;
                }
            }
        }
        return true;
    }
}
