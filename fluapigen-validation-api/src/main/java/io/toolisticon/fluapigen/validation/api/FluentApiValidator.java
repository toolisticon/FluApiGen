package io.toolisticon.fluapigen.validation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Meta annotation to declare validators via annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface FluentApiValidator {

    /**
     * The validator to be applied.
     * @return
     */
    Class<? extends Validator> value();

    /**
     * Used to map parameters to validator constructor.
     * Defaults to empty array for no-arg constructor.
     * @return the parameter names
     */
    String[] attributeNamesToConstructorParameterMapping() default {};

    /**
     * Must reference annotation types annotated with this meta annotation.
     * Defaults to empty array.
     * @return the annotations overwritten with this annotation
     */
    Class<?>[] overwrites() default {};

}
