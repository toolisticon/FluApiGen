package io.toolisticon.fluapigen.validation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks if string has maximal length.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@FluentApiValidator(value = MaxLength.ValidatorImpl.class, attributeNamesToConstructorParameterMapping = {"value"})
public @interface MaxLength {

    int value();

    class ValidatorImpl implements Validator<String> {

        private final int maxLength;


        public ValidatorImpl(int maxLength) {

            this.maxLength = maxLength;

        }

        @Override
        public boolean validate(String obj) {
            return obj == null || obj.length() <= maxLength;
        }

    }

}


