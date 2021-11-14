package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures a Backing Bean field.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BackingBeanField {

    /**
     * The kind
     */
    public enum Kind {
        SINGLE,
        COLLECTION
    }

    /**
     * The name of the backing bean field.
     *
     * @return
     */
    String value();

    /**
     * The default value as a String.
     * The framework will try to convert the value to the corresponding type.
     *
     * @return
     */
    String defaultValue() default "";
}
