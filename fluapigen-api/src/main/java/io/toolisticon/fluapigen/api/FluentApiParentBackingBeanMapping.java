package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures mapping of a fluent api method parameter to backing bean value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
@Repeatable(FluentApiParentBackingBeanMappings.class)
public @interface FluentApiParentBackingBeanMapping {
    /**
     * The id of the corresponding backing bean value.
     *
     * @return The id of the corresponding backing bean value
     */
    String value();

    /**
     * Defines the target backing bean to set the value for.
     * Will be ignored if placed on method. This is usually the case if backing bean is added to parent.
     * @return
     */
    TargetBackingBean target() default TargetBackingBean.THIS;


    /**
     * Defines how the value should be mapped.
     * It can set the value or be added to a Collection based value.
     * @return The action type or SET as a default
     */
    MappingAction action() default MappingAction.SET;

}
