package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation has two use cases:
 * 1. Marks a command if placed on type (value must not be set)
 * 2. Links a command if placed on method (value must link to type annotated with FluentApiCommand)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface FluentApiCommand {
    /**
     * Defines the command class to link with method.
     * This must only be set as an attribute if placed on a fluent api interface method.
     * @return
     */
    Class<?> value() default Void.class;
}
