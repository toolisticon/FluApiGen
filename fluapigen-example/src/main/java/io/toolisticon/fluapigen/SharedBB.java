package io.toolisticon.fluapigen;

import io.toolisticon.fluapigen.api.FluentApiBackingBean;

public class SharedBB {

    @FluentApiBackingBean
    public interface MySharedBB {

        String name();

    }
}
