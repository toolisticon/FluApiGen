package io.toolisticon.fluapigen.validation.api;


import java.util.regex.Pattern;

@FluentApiValidator(value = Matches.ValidatorImpl.class, parameterNames = {"value"})
public @interface Matches {

    String value();

    class ValidatorImpl implements Validator<String> {

        private final String regularExpression;

        public ValidatorImpl(String regularExpression) {
            this.regularExpression = regularExpression;
        }

        @Override
        public boolean validate(String obj) {
            return Pattern.compile(regularExpression).matcher(obj).matches();
        }

    }

}
