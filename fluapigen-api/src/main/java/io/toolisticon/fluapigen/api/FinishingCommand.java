package io.toolisticon.fluapigen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that configures a finishing command that should be called.
 * <p>
 * Target classes must contain a static method that takes the Backing Bean class as its first and only parameter.
 * targetId attribute can be used to select Finishing command, if more than one finishing command implementation is present in class.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FinishingCommand {

    /**
     * The class that contains the target command method.
     */
    Class<?> value();

    /**
     * The command method selector if class contains more than one method annotated with {@link FinishingCommandId}.
     *
     * @return The target id. Defaults to empty String.
     */
    String targetId() default "";

}
