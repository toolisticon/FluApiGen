package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Optionally useed to annotate a backing bean field.
 * Allows to customize the backing bean fields id or to configure an initial value.
 *
 * Must be used in class annotated with {@link FluentApiBackingBean} annotation.
 *
 * The id will be used as a reference by {@link FluentApiBackingBeanMapping},  {@link FluentApiParentBackingBeanMapping} or {@link FluentApiImplicitValue}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface FluentApiBackingBeanField {
    /**
     * The id of the backing bean field.
     * Defaults to the method name if not set explicitly.
     * @return the id
     */
    String value() default "";

    /**
     * The initial value to be set.
     * The array must contain only one value for all collection and array related types.
     * If set the corresponding field will be initialized, even if passed array is just empty.
     * @return the initial value to be set
     */
    String[] initValue() default {};
}
