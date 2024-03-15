package io.toolisticon.fluapigen.validation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Validates String to be not empty.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@FluentApiValidator(NotEmpty.ValidatorImpl.class)
public @interface NotEmpty {

    class ValidatorImpl implements Validator<String> {
        @Override
        public boolean validate(String object) {
            return object == null || !object.isEmpty();
        }
    }

}
