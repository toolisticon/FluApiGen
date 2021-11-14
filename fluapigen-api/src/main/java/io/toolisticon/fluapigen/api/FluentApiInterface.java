package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to bind an interface with a backing bean interface.
 * Annotated interface must reside in a class annotated with a {@link FluentApi} annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FluentApiInterface {

    /**
     * Mapping to BackingBean
     * @return The backing bean class
     */
    Class<?> value();

}
