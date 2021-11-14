package io.toolisticon.fluapigen.api;

public enum TargetBB {
    /**
     * Sets target to fluent interfaces backing bean
     */
    SELF,
    /**
     * Sets target to the backing bean of the next fluent interface.
     * In this case the Return value must reference another fluent interface (Transition)
     */
    NEXT;
}
