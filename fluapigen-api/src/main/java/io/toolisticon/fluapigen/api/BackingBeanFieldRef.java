package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to configure a backing bean field reference at for fluent api methods.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BackingBeanFieldRef {

    /**
     * The name of the field to be referenced.
     * @return
     */
    String value();

    /**
     * Configures which Backing Bean should be referenced.
     * <p>
     * Defaults to TargetBB.SELF
     *
     * @return
     */
    TargetBB target() default TargetBB.SELF;
}
