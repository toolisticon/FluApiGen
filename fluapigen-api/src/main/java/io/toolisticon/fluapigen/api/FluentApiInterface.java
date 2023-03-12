package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Fluent Api interface
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface FluentApiInterface {
    /**
     * The backing bean class to fill
     *
     * @return the backing bean class that is filled by the interface
     */
    Class<?> value();

    /**
     *
     * @return
     */
    Class<?> parent() default Void.class;
}
