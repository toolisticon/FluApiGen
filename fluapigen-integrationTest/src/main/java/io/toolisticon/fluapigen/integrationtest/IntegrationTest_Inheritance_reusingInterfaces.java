package io.toolisticon.fluapigen.integrationtest;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import java.io.Serializable;

@FluentApi("InheritanceIntegrationTestStarter")
public class IntegrationTest_Inheritance_reusingInterfaces {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyRootLevelBackingBean {

        @FluentApiBackingBeanField("1st")
        FirstInheritedBackingBean get1st();

        @FluentApiBackingBeanField("2nd")
        SecondInheritedBackingBean get2nd();

    }


    interface MyReusedBackingBeanFields {

        @FluentApiBackingBeanField("name")
        String getName();

    }


    @FluentApiBackingBean
    interface FirstInheritedBackingBean extends MyReusedBackingBeanFields {

        @FluentApiBackingBeanField("1st")
        String get1st();

    }

    @FluentApiBackingBean
    interface SecondInheritedBackingBean extends MyReusedBackingBeanFields {

        @FluentApiBackingBeanField("2nd")
        String get2nd();

    }


    // Fluent Api interfaces
    @FluentApiInterface(MyRootLevelBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        My1stInterface goto1st();

        My2ndInterface goto2nd();

        @FluentApiCommand(MyCommand.class)
        MyRootLevelBackingBean myCommand();


    }


    public interface SharedInterface<FLUENT_INTERFACE> {

        FLUENT_INTERFACE setName(@FluentApiBackingBeanMapping(value = "name") String name);

    }


    @FluentApiInterface(FirstInheritedBackingBean.class)
    public interface My1stInterface extends SharedInterface<My1stInterface> {

        @FluentApiParentBackingBeanMapping("1st")
        MyRootInterface set1st(@FluentApiBackingBeanMapping(value = "1st") String first);

    }

    @FluentApiInterface(SecondInheritedBackingBean.class)
    public interface My2ndInterface extends SharedInterface<My2ndInterface> {

        @FluentApiParentBackingBeanMapping("2nd")
        MyRootInterface set1st(@FluentApiBackingBeanMapping(value = "2nd") String second);

    }

    // Commands
    @FluentApiCommand
    static class MyCommand {
        static MyRootLevelBackingBean myCommand(MyRootLevelBackingBean backingBean) {
            System.out.println("CHECK");
            return backingBean;
        }
    }


}