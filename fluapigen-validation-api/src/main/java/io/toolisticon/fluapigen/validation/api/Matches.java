package io.toolisticon.fluapigen.validation.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@FluentApiValidator(value = Matches.ValidatorImpl.class, attributeNamesToConstructorParameterMapping = {"value"})
public @interface Matches {

    String value();

    class ValidatorImpl implements Validator<String> {

        private final String regularExpression;

        public ValidatorImpl(String regularExpression) {
            this.regularExpression = regularExpression;
        }

        @Override
        public boolean validate(String obj) {
            return obj == null || Pattern.compile(regularExpression).matcher(obj).matches();
        }

    }

}
