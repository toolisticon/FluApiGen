package io.toolisticon.fluapigen.validation.api;

import java.lang.reflect.Array;
import java.util.Collection;

@FluentApiValidator(NotEmpty.ValidatorImpl.class)
public @interface NotEmpty {

    class ValidatorImpl implements Validator<Object> {
        @Override
        public boolean validate(Object obj) {

            if (obj.getClass().isArray()) {
                return Array.getLength(obj) > 0;
            } else if (obj instanceof Collection) {
                return !((Collection) obj).isEmpty();
            } else if (obj instanceof String) {
                return !((String) obj).isEmpty();
            }
            return obj != null;
        }
    }

}
