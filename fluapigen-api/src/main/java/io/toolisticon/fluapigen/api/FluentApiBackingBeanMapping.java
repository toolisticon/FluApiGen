package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configures mapping of a fluent api method parameter to a backing bean value.
 * Must be used inside a class annotated with {@link FluentApiInterface} annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.PARAMETER, ElementType.METHOD})
@Documented
public @interface FluentApiBackingBeanMapping {
    /**
     * The id of the corresponding backing bean value.
     *
     * @return The id of the corresponding backing bean value
     */
    String value();

    /**
     * Defines the target backing bean to set the value for.
     * This can be either the current backing bean defined via the {@link FluentApiInterface} annotation or the next backing bean defined by the methods return value.
     * In this case the return value must correspond to an interface annotated with the {@link FluentApiInterface}.
     * @return The targeted backing bean, defaults to THIS (== the current backing bean)
     */
    TargetBackingBean target() default TargetBackingBean.THIS;


    /**
     * Defines how a value should be applied.
     * Currently, it's possible to set a value or to add a value to a supported Collection type.
     * @return The mapping action to use. Defaults to MappingAction.SET
     */
    MappingAction action() default MappingAction.SET;

    /**
     * Converter to use for non-matching fluent interface parameter types.
     * @return the converted value.
     */
    Class<? extends FluentApiConverter> converter () default FluentApiConverter.NoConversion.class;

}
