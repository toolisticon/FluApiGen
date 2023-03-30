package io.toolisticon.fluapigen.processor.tests;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;

import javax.annotation.Generated;

@FluentApi("Xyz")
public class TestcaseInvalidNumberOfParentMappingsAtCommand {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyBackingBean {

        @FluentApiBackingBeanField("childConfig")
        MyMidBackingBean childConfig();

    }

    @FluentApiBackingBean
    interface MyMidBackingBean {
        @FluentApiBackingBeanField("childConfig")
        MyLowBackingBean childConfig();
    }

    @FluentApiBackingBean
    interface MyLowBackingBean {

        @FluentApiBackingBeanField("value")
        String value();

    }

    // Fluent Api interfaces
    @FluentApiInterface(MyBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        MyLowInterface addLowConfig();


    }

    // Fluent Api interfaces
    @FluentApiInterface(MyMidBackingBean.class)
    public interface MyMidInterface {

        MyLowInterface addLowConfig();

    }

    @FluentApiInterface(MyLowBackingBean.class)
    public interface MyLowInterface {



        @FluentApiCommand(MyCommand.class)
        @FluentApiParentBackingBeanMapping(value = "childConfig",action = MappingAction.SET)
        //@FluentApiParentBackingBeanMapping(value = "childConfig",action = MappingAction.SET)
        void addLowConfig(@FluentApiBackingBeanMapping("value") String value);

    }

    // Commands
    @FluentApiCommand
    static class MyCommand{
        static void myCommand (MyBackingBean backingBean) {
            System.out.println(backingBean.childConfig().childConfig().value());
        }
    }




}