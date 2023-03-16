package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Repeatable annotation to implicitly set one or more values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Repeatable(FluentApiImplicitValues.class)
@Documented
public @interface FluentApiImplicitValue {

    /**
     * The id of the backing bean field to set.
     * @return the id of the backing bean field
     */
    String id();

    /**
     * The value to set. This will be converted from string according to it's backing field type.
     * @return the value as a string to set
     */
    String value();

    /**
     * The target to set the value for. Might be either current backing bean or next one.
     * Defaults to current.
     * @return The target backing bean
     */
    TargetBackingBean target() default TargetBackingBean.THIS;

}
