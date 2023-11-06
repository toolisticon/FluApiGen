package io.toolisticon.fluapigen.processor.tests;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.validation.api.Matches;

@FluentApi("Xyz")
public class TestcaseValidator {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyBackingBean {

        @FluentApiBackingBeanField("name")
        String getName();

    }

    // Fluent Api interfaces
    @FluentApiInterface(MyBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        MyRootInterface setName(@Matches("aaa.*") @FluentApiBackingBeanMapping("name") String name);

        @FluentApiCommand(MyCommand.class)
        void myCommand();

    }

    // Commands
    @FluentApiCommand
    static class MyCommand{
        static void myCommand (MyBackingBean backingBean) {
            System.out.println(backingBean.getName());
        }
    }




}