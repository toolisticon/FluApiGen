package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures a fluent api.
 * Annotation must be placed on class.
 *
 * The class should have inner interfaces that are defining the Backing beans and the fluent interfaces to use.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FluentApi {

    /**
     * Declares the root interface.
     * The referenced interface must reside in the class annotated with this annotation.
     * @return The root interface type
     */
    Class<?> rootInterface();

    /**
     * Configures the class name of the builder.
     * @return the builder class name
     */
    String builderClassName();

}
