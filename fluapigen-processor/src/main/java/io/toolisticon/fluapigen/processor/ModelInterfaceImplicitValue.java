package io.toolisticon.fluapigen.processor;

public class ModelInterfaceImplicitValue {


    private final ModelBackingBeanField backingBeanField;

    private final FluentApiImplicitValueWrapper annotation;

    public ModelInterfaceImplicitValue(ModelBackingBeanField backingBeanField, FluentApiImplicitValueWrapper annotation) {
        this.backingBeanField = backingBeanField;
        this.annotation = annotation;
    }

    public String getBackingBeanFieldName() {
        return backingBeanField.getFieldName();
    }

    public String getValueAssignmentString() {
        if (backingBeanField.getFieldType().isEnum()) {
            // handle enum
            return handleEnum();
        } else {

            switch (backingBeanField.getFieldType().getQualifiedName()) {
                case "java.lang.String": {
                    return "\"" + annotation.value() + "\"";
                }
                case "boolean":
                case "java.lang.Boolean": {
                    return Boolean.valueOf(annotation.value()).toString();
                }
                case "int":
                case "java.lang.Integer": {
                    return Integer.valueOf(annotation.value()).toString();
                }
                case "long":
                case "java.lang.Long": {
                    return Long.valueOf(annotation.value()).toString();
                }
                case "float":
                case "java.lang.Float": {
                    return Float.valueOf(annotation.value()).toString();
                }
                case "double":
                case "java.lang.Double": {
                    return Float.valueOf(annotation.value()).toString();
                }

            }

            return "";
        }
    }

    private String handleEnum() {
        return backingBeanField.getFieldType().getSimpleName() + "." + annotation.value();
    }

}
