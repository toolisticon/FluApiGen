package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Must be placed on interface declared inside a class annotated with the {@link FluentApi} annotation.
 *
 * interface should contain getter methods for all backing bean fields. (non void return type and no parameters)
 * Implementing class will be generated by the processor.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BackingBean {

}
