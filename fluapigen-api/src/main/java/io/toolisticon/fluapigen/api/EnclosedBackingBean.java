package io.toolisticon.fluapigen.api;

public @interface EnclosedBackingBean {

    enum Type {
        SINGLE_VALUE,
        LIST,
        SET;
    }

    String fieldName();

    String enclosedBackingBean();

    Type type() default Type.SINGLE_VALUE;

}
