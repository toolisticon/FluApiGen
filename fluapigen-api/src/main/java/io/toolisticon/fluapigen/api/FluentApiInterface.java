package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a fluent api interface.
 * Must be placed on interface declared in class annotated with {@link FluentApi}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface FluentApiInterface {
    /**
     * The backing bean class to use by the fluent api interface.
     *
     * @return the backing bean class to use by the fluent api interface
     */
    Class<?> value();

}
