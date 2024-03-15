package io.toolisticon.fluapigen.validation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Checks if string has mininmal length.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@FluentApiValidator(value = MinLength.ValidatorImpl.class, attributeNamesToConstructorParameterMapping = {"value"})
public @interface MinLength {

    int value();

    class ValidatorImpl implements Validator<String> {

        private final int minLength;

        public ValidatorImpl(int minLength) {
            this.minLength = minLength;
        }

        @Override
        public boolean validate(String object) {

            return object == null || object.length() >= minLength;

        }
    }

}


