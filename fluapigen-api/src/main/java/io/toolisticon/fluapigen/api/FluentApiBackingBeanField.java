package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add id to backing bean field.
 * Used together with {@link FluentApiBackingBeanMapping}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface FluentApiBackingBeanField {
    /**
     * The id of the backing bean field.
     * @return the id
     */
    String value();

    /**
     * The initial value to be set
     * @return the initial value to be set
     */
    String initValue() default "";
}
