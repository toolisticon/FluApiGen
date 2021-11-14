package io.toolisticon.fluapigen.processor;

import io.toolisticon.aptk.annotationwrapper.api.CustomCodeMethod;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.corematcher.CoreMatchers;
import io.toolisticon.aptk.tools.fluentfilter.FluentElementFilter;
import io.toolisticon.aptk.tools.fluentvalidator.FluentElementValidator;
import io.toolisticon.fluapigen.api.BackingBean;

import javax.lang.model.element.ExecutableElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BackingBeanWrapperCustomCode {

    public static class BackingBeanField {

        final ExecutableElement fieldGetterMethodElement;

        BackingBeanField(ExecutableElement fieldGetterMethodElement) {
            this.fieldGetterMethodElement = fieldGetterMethodElement;
        }

        public TypeMirrorWrapper getFieldType() {
            return TypeMirrorWrapper.wrap(this.fieldGetterMethodElement.getReturnType());
        }

        public String getFieldName() {

            String fieldName = fieldGetterMethodElement.getSimpleName().toString();

            if (fieldName.length() > 3 && fieldName.startsWith("get")) {

                fieldName = fieldName.substring(3);
                fieldName = fieldName.substring(0, 1).toLowerCase() + (fieldName.length() > 1 ? fieldName.substring(1) : "");

            } else if (fieldName.length() > 2 && fieldName.startsWith("is")) {
                fieldName = fieldName.substring(2);
                fieldName = fieldName.substring(0, 1).toLowerCase() + (fieldName.length() > 1 ? fieldName.substring(1) : "");
            }

            return fieldName;
        }

        /**
         * Method to create a prefixed camel cased String
         *
         * @param name   the name to be prefixed
         * @param prefix the prefix
         * @return the camel cased string
         */

        static String getPrefixedName(String prefix, String name) {
            return prefix + name.substring(0, 1).toUpperCase() + name.substring(1);
        }

        public String getGetterName() {
            return this.fieldGetterMethodElement.getSimpleName().toString();
        }

        public String getSetterName() {
            String fieldName = getFieldName();
            return "set" + fieldName.substring(0, 1).toUpperCase() + (fieldName.length() > 1 ? fieldName.substring(1) : "");
        }


    }

    @CustomCodeMethod(BackingBean.class)
    public static String getSimpleName(BackingBeanWrapper wrapper) {
        return TypeMirrorWrapper.wrap(wrapper._annotatedElement().asType()).getSimpleName();
    }

    @CustomCodeMethod(BackingBean.class)
    public static TypeMirrorWrapper getInterfaceTypeMirror(BackingBeanWrapper wrapper) {
        return TypeMirrorWrapper.wrap(wrapper._annotatedElement().asType());
    }

    @CustomCodeMethod(BackingBean.class)
    public static BackingBeanField[] getBackingBeanFields(BackingBeanWrapper wrapper) {

        // First need to filter all getter
        List<ExecutableElement> getterElements = FluentElementFilter.createFluentElementFilter(wrapper._annotatedElement().getEnclosedElements())
                .applyFilter(CoreMatchers.IS_METHOD)
                .applyInvertedFilter(CoreMatchers.HAS_VOID_RETURN_TYPE)
                .applyFilter(CoreMatchers.HAS_NO_PARAMETERS)
                .getResult();

        BackingBeanField[] fields = new BackingBeanField[getterElements.size()];

        int i = 0;
        for (ExecutableElement executableElement : getterElements) {

            fields[i] = new BackingBeanField(executableElement);

            i++;
        }

        return fields;
    }

    @CustomCodeMethod(BackingBean.class)
    public static Set<String> getImports(BackingBeanWrapper wrapper) {

        Set<String> imports = new HashSet<>();

        // Since it is an inner class we must add it
        imports.add(wrapper.getInterfaceTypeMirror().getQualifiedName());
        // So just need to add field types

        for (BackingBeanField backingBeanField : wrapper.getBackingBeanFields()) {
            imports.addAll(backingBeanField.getFieldType().getImports());
        }

        return imports;
    }

    @CustomCodeMethod(BackingBean.class)
    public static boolean validate(BackingBeanWrapper wrapper) {
        boolean result = true;

        List<ExecutableElement> getters = FluentElementFilter.createFluentElementFilter(wrapper._annotatedElement())
                .applyFilter(CoreMatchers.IS_METHOD)
                .getResult();

        for (ExecutableElement executableElement : getters) {
            result = result & FluentElementValidator.createFluentElementValidator(executableElement)
                    .applyValidator(CoreMatchers.HAS_NO_PARAMETERS)
                    .applyInvertedValidator(CoreMatchers.HAS_VOID_RETURN_TYPE)
                    .validateAndIssueMessages();
        }

        return result;
    }

}
