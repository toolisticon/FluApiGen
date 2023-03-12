package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as fluent api holder.
 * Only interfaces annotated with {@link FluentApiInterface}, {@link } will be part of the Fluent API.
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
