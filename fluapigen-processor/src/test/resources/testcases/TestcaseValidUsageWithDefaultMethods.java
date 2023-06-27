package io.toolisticon.fluapigen.processor.tests;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;

@FluentApi("Xyz")
public class TestcaseValidUsageWithDefaultMethods {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyBackingBean {

        @FluentApiBackingBeanField("name")
        String getName();

        default String aDefaultMethod() {
            return "IT WORKS";
        }

    }

    // Fluent Api interfaces
    @FluentApiInterface(MyBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        MyRootInterface setName(@FluentApiBackingBeanMapping("name") String name);

        @FluentApiCommand(MyCommand.class)
        void myCommand();

        default String aDefaultMethod() {
            return "IT WORKS";
        }

    }

    // Commands
    @FluentApiCommand
    static class MyCommand{
        static void myCommand (MyBackingBean backingBean) {
            System.out.println(backingBean.getName());
        }
    }




}