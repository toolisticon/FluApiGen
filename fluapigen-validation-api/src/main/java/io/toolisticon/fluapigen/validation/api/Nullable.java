package io.toolisticon.fluapigen.validation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Needed to overrule {@link NotNull} annotations on method or enclosing interfaces/types.
 * All parameters are by default nullable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@FluentApiValidator(value = Nullable.ValidatorImpl.class, overwrites = NotNull.class)
public @interface Nullable {

    class ValidatorImpl implements Validator<Object> {
        @Override
        public boolean validate(Object object) {
            return true;
        }

        @Override
        public boolean validate(Object[] array) {
            return true;
        }
    }

}
