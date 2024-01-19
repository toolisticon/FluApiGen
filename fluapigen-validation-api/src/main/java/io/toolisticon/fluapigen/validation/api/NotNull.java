package io.toolisticon.fluapigen.validation.api;

@FluentApiValidator(NotNull.ValidatorImpl.class)
public @interface NotNull {

    class ValidatorImpl implements Validator<Object> {
        @Override
        public boolean validate(Object obj) {
            return obj != null;
        }

        @Override
        public boolean validate(Object[] obj) {
            return obj != null && Validator.super.validate(obj);
        }
    }

}
