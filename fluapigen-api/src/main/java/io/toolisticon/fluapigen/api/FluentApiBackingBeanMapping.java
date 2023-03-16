package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures mapping of method parameter to backing bean value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface FluentApiBackingBeanMapping {
    /**
     * The name of the corresponding backing bean value.
     *
     * @return The name of the corresponding backing bean value
     */
    String value();

    /**
     * Defines the target backing bean to set the value for.
     * Will be ignored if placed on method. This is usually the case if backing bean is added to parent.
     * @return
     */
    TargetBackingBean target() default TargetBackingBean.THIS;

    /**
     * The mapping action. Allows to define different actions to be done during mapping to backing bean.
     * Defaults to set, For collections. We will ignore this for the moment
     * @return
     */
    MappingActionType mappingAction() default MappingActionType.SET;

}
