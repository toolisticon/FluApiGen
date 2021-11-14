package io.toolisticon.fluapigen.processor;

class Constants {

    static class FluentApiAttributeNames {
        final static String ROOT_INTERFACE = "rootInterface";
        final static String BUILDER_CLASS_NAME = "builderClassName";
    }

    static class BackingBeanAttributeNames {
        final static String ID = "id";
        final static String CLASS_NAME = "backingBeanClassName";
        final static String ASSOCIATE_INTERFACES = "associatedInterfaces";
        final static String BACKING_BEAN_FIELDS = "backingBeanFields";
    }

    static class BackingBeanFieldAttributeNames {
        final static String VALUE = "value";
        final static String FIELD_TYPE = "fieldType";
        final static String DEFAULT_VALUE = "defaultValue";
    }

    static class ImplicitValueAttributeNames {
        final static String FIELD_NAME = "fieldName";
        final static String FIELD_TYPE = "fieldType";
    }


}
