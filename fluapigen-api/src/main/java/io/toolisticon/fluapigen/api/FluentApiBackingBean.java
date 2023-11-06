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
 *
 * The annotated inner interface must only contain methods without parameter and a non-void return type.
 * Those methods will be mapped by fluent interface method parameters, return types or implicit values.
 *
 * Therefore, each method will have its method name as a backing bean unique id.
 * This id can be customized by the {@link FluentApiBackingBeanField}.
 * Additionally, an init value can be defined by that annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface FluentApiBackingBean {
}
