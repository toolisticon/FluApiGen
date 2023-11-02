package io.toolisticon.fluapigen.api.validation;

@FluentApiValidator(NotNull.ValidatorImpl.class)
public @interface NotNull {

    class ValidatorImpl implements Validator<Object> {
        @Override
        public boolean validate(Object obj) {
            return obj != null;
        }
    }

}
