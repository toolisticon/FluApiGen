package io.toolisticon.fluapigen.api;

/**
 * Defines target of a backing bean mapping.
 */
public enum TargetBackingBean {
    /** Used to set value at the current backing bean */
    THIS,
    /** Used to set value at the next backing bean, which relates with return values backing bean type. */
    NEXT;
}
