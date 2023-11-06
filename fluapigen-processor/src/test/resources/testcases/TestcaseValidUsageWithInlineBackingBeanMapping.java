package io.toolisticon.fluapigen.processor.tests;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInlineBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import java.util.List;

@FluentApi("Xyz")
public class TestcaseValidUsageWithInlineBackingBeanMapping {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyBackingBean {

        @FluentApiBackingBeanField("name")
        String getName();

        List<MySubBackingBean> subBackingBeans();


    }

    @FluentApiBackingBean
    interface MySubBackingBean {

        String value1();

        String value2();

    }

    // Fluent Api interfaces
    @FluentApiInterface(MyBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        @FluentApiInlineBackingBeanMapping(value = "subBackingBeans")
        @FluentApiImplicitValue(id = "value2", value = "WOW", target = TargetBackingBean.INLINE)
        MyRootInterface setName(@FluentApiBackingBeanMapping("name") String name, @FluentApiBackingBeanMapping(value = "value1", target = TargetBackingBean.INLINE) String value1);

        @FluentApiCommand(MyCommand.class)
        void myCommand();

    }

    // Commands
    @FluentApiCommand
    static class MyCommand {
        static void myCommand(MyBackingBean backingBean) {
            System.out.println(backingBean.getName());
        }
    }


}