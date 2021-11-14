package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares a target method for a finishing command.
 *
 * The target id name must be unique within a class.
 *
 * Classes that contain a method annotated with this annotation must be visible by the caller.
 *
 * Annotated methods must be:
 *  - static
 *  - take one parameter
 *  - not private and visible and accessible for the caller
 *
 *  Annotated methods may have a return type.
 *  If so, then
 *   - callers return type must be void
 *   - or match the callers return type
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FinishingCommandId {

    /**
     * The target id.
     * Must only be set if class contains multiple methods annotated with this annotation.
     * @return the target id, or an empty String as default.
     */
    String value() default("");
}
