package io.toolisticon.fluapigen.api.validation;

import java.lang.reflect.Array;
import java.util.Collection;


@FluentApiValidator(value = MaxLength.ValidatorImpl.class, parameterNames = {"value"})
public @interface MaxLength {

    int value();

    class ValidatorImpl implements Validator<Object> {

        private final int maxLength;

        public ValidatorImpl(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public boolean validate(Object obj) {

            if (obj.getClass().isArray()) {
                return Array.getLength(obj) <= maxLength;
            } else if (obj instanceof Collection) {
                return ((Collection) obj).size() <= maxLength;
            } else if (obj instanceof String) {
                return ((String) obj).length() <= maxLength;
            }
            return obj != null;
        }
    }

}


