package io.toolisticon.fluapigen.processor;

import io.toolisticon.fluapigen.api.FluentApiImplicitValue;

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
                    return "\""+annotation.value()+"\"";
                }
                case "boolean":
                case "java.lang.Boolean": {
                    return Boolean.valueOf(annotation.value()).toString();
                }
            }

            return "";
        }
    }

    private String handleEnum() {
        return backingBeanField.getFieldType().getSimpleName() + "." + annotation.value();
    }

}
