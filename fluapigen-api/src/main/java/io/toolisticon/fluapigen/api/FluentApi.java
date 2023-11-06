package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class that contains a fluent api related interfaces and commands.
 *
 * This annotation will be processed by the annotation processor.
 * The annotated class will be scanned for fluent api or backing bean interfaces and commands.
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface FluentApi {

    /**
     * The name of the Fluent Api starter class.
     * Class will be created in same package as the annotated class.
     * @return the value
     */
    String value();


}
