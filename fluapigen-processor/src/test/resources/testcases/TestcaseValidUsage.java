package io.toolisticon.fluapigen.processor.tests;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import javax.annotation.Generated;

@FluentApi("Xyz")
public class TestcaseValidUsage {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyBackingBean {

        String getName();

    }

    // Fluent Api interfaces
    @FluentApiInterface(MyBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        MyRootInterface setName(String name);

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