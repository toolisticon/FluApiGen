package io.toolisticon.fluapigen.api;

/**
 * Defines target of a backing bean mapping.
 */
public enum TargetBackingBean {
    /** Used to set value at the current backing bean */
    THIS,
    /** Used to set value at the next backing bean, which relates with return values backing bean type. */
    NEXT,
    /** A backing bean will be created and set or added to a value of this or next backing bean. */
    INLINE;
}
