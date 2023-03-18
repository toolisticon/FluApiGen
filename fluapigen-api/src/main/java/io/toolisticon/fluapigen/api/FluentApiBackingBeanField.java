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
     * The initial value to be set.
     * The array must contain only one value for all collection and array related types.
     * If set the corresponding field will be initialized, even if passed array is just empty.
     * @return the initial value to be set
     */
    String[] initValue() default {};
}
