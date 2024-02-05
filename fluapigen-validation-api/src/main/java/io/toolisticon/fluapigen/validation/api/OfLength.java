package io.toolisticon.fluapigen.validation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checks if string has specific length.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@FluentApiValidator(value = OfLength.ValidatorImpl.class, attributeNamesToConstructorParameterMapping = {"value", "target"})
public @interface OfLength {

    int value();

    class ValidatorImpl implements Validator<String> {

        private final int ofLength;


        public ValidatorImpl(int ofLength) {

            this.ofLength = ofLength;

        }

        @Override
        public boolean validate(String obj) {
            return obj == null || obj.length() == ofLength;
        }

    }

}


