package io.toolisticon.fluapigen.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The processing state.
 */
public class FluentApiState {

    private final FluentApiWrapper fluentApiWrapper;
    private final List<FluentApiInterfaceWrapper> fluentApiInterfaceWrappers;
    private final List<FluentApiBackingBeanWrapper> fluentApiBackingBeanWrappers;
    private final List<FluentApiCommandWrapper> fluentApiCommandWrappers;

    public FluentApiState(FluentApiWrapper fluentApiWrapper, List<FluentApiInterfaceWrapper> fluentApiInterfaceWrappers, List<FluentApiBackingBeanWrapper> fluentApiBackingBeanWrappers, List<FluentApiCommandWrapper> fluentApiCommandWrappers) {
        this.fluentApiWrapper = fluentApiWrapper;
        this.fluentApiInterfaceWrappers = fluentApiInterfaceWrappers;
        this.fluentApiBackingBeanWrappers = fluentApiBackingBeanWrappers;
        this.fluentApiCommandWrappers = fluentApiCommandWrappers;
    }

    public FluentApiWrapper getFluentApiWrapper() {
        return fluentApiWrapper;
    }

    public List<FluentApiInterfaceWrapper> getFluentApiInterfaceWrappers() {
        return fluentApiInterfaceWrappers;
    }

    public List<FluentApiBackingBeanWrapper> getFluentApiBackingBeanWrappers() {
        return fluentApiBackingBeanWrappers;
    }

    public List<FluentApiCommandWrapper> getFluentApiCommandWrappers() {
        return fluentApiCommandWrappers;
    }



    public boolean validate() {

        boolean result = fluentApiWrapper.validate();
        for (FluentApiInterfaceWrapper wrapper : fluentApiInterfaceWrappers) {
            result = result & wrapper.validate();
        }

        for (FluentApiBackingBeanWrapper wrapper : fluentApiBackingBeanWrappers) {
            result = result & wrapper.validate();
        }

        for (FluentApiCommandWrapper wrapper : fluentApiCommandWrappers) {
            result = result & wrapper.validate();
        }

        return result;
    }
}
