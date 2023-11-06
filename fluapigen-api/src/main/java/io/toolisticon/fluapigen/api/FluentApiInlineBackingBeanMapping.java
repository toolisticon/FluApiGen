package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Some backing beans have very few parameters so that they can easily set inline.
 * The backing bean will be created and added/set as an attribute at fluent apis backing bean or the backing bean of the returned fluent interface.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD})
@Documented
public @interface FluentApiInlineBackingBeanMapping {

    /**
     * The id of the corresponding backing bean value.
     *
     * @return The id of the corresponding backing bean value
     */
    String value();

}
