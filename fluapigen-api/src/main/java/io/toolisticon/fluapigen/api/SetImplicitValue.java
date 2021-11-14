package io.toolisticon.fluapigen.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to configure an implicit setting of a field value.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SetImplicitValue {
    /**
     * The implicit value to set.
     * Will be converted to corresponding type.
     *
     * @return The implicit value to set
     */
    String value();
}
