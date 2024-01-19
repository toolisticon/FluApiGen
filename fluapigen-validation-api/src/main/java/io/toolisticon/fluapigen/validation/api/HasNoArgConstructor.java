package io.toolisticon.fluapigen.validation.api;


import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * Meta annotation to declare validators via annotation.
 */
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
                    // need to get constructor method
                    Constructor<?> constructor = obj.getConstructor();

                    for (int modifier : modifiers) {
                        if ((modifier & obj.getModifiers()) == 0) {
                            return false;
                        }
                    }

                } catch (NoSuchMethodException e) {
                    return false;
                }
            }

            return true;
        }
    }


}
