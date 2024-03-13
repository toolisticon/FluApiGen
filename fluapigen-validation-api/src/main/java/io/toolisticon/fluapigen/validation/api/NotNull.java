package io.toolisticon.fluapigen.validation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

/**
 * Validates if values isn't null.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@FluentApiValidator(value = NotNull.ValidatorImpl.class, overwrites = {Nullable.class})
public @interface NotNull {

    class ValidatorImpl implements Validator<Object> {
        @Override
        public boolean validate(Object object) {
            return object != null;
        }

        @Override
        public boolean validate(Object[] array) {
            return array != null && Validator.super.validate(array);
        }

        @Override
        public boolean validate(Iterable<Object> iterable){
            return iterable != null && Validator.super.validate(iterable);
        }
    }

}
