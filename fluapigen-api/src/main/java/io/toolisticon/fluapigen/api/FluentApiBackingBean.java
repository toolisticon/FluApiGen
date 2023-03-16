package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an interface nested inside a class annotated with {@link FluentApi} as a backing bean.
 * Backing beans are the data holder classes used to store the data configured via the fluent api.
 * The root backing bean can be transferred to a class annotated with {@link FluentApiCommand}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface FluentApiBackingBean {
}
