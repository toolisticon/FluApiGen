package io.toolisticon.fluapigen.validation.api;

import java.lang.reflect.Array;
import java.util.Collection;


@FluentApiValidator(value = MinLength.ValidatorImpl.class, attributeNamesToConstructorParameterMapping = {"value"})
public @interface MinLength {

    int value();

    class ValidatorImpl implements Validator<Object> {

        private final int minLength;

        public ValidatorImpl(int minLength) {
            this.minLength = minLength;
        }

        @Override
        public boolean validate(Object obj) {

            if (obj.getClass().isArray()) {
                return Array.getLength(obj) >= minLength;
            } else if (obj instanceof Collection) {
                return ((Collection) obj).size() >= minLength;
            } else if (obj instanceof String) {
                return ((String) obj).length() >= minLength;
            }
            return obj != null;
        }
    }

}


