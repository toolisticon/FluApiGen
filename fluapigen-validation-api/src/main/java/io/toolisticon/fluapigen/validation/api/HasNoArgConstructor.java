package io.toolisticon.fluapigen.validation.api;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Meta annotation to declare validators via annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@FluentApiValidator(value = HasNoArgConstructor.ValidatorImpl.class, attributeNamesToConstructorParameterMapping = {"modifier"})
public @interface HasNoArgConstructor {

    int[] modifier() default Modifier.PUBLIC;

    class ValidatorImpl implements Validator<Class> {

        private final int[] modifiers;

        public ValidatorImpl(int[] modifiers) {
            this.modifiers = modifiers;
        }

        @Override
        public boolean validate(Class obj) {

            if (obj != null) {

                try {

                    // check if class is abstract
                    if ((obj.getModifiers() & Modifier.ABSTRACT) != 0){
                        return false;
                    }

                    // need to get constructor method
                    Constructor<?> constructor = obj.getDeclaredConstructor();

                    for (int modifier : modifiers) {
                        if ((modifier & obj.getModifiers()) == 0) {
                            return false;
                        }
                    }

                } catch (NoSuchMethodException e) {

                    // must check if there are any explicit constructors, if not then there is just the default public one.
                    return obj.getDeclaredConstructors().length == 0 && hasPublicModifier();

                }
            }

            return true;
        }

        boolean hasPublicModifier () {
            for (int modifier : modifiers){
                if (modifier == Modifier.PUBLIC) {
                    return true;
                }
            }
            return false;
        }
    }


}
