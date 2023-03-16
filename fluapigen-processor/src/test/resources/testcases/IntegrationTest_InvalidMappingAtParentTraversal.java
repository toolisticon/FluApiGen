package io.toolisticon.fluapigen.testcases;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import java.util.List;

@FluentApi("IntegrationTestStarter")
public class IntegrationTest_InvalidMappingAtParentTraversal {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyRootLevelBackingBean {

        @FluentApiBackingBeanField("name")
        String getName();

        @FluentApiBackingBeanField("midLevelBB")
        List<MyMidLevelBackingBean> midLevelBB();

    }

    @FluentApiBackingBean
    interface MyMidLevelBackingBean {

        @FluentApiBackingBeanField("lowLevelBBs")
        List<MyLowLevelBackingBean> getLowLevelBB();

    }

    @FluentApiBackingBean
    interface MyLowLevelBackingBean {

        @FluentApiBackingBeanField("stringValue")
        String getStringValue();

        @FluentApiBackingBeanField("primitiveBooleanValue")
        boolean getPrimitiveBooleanValue();

        @FluentApiBackingBeanField("booleanValue")
        Boolean getBooleanValue();

        @FluentApiBackingBeanField("primitiveIntValue")
        int getPrimitiveIntValue();

        @FluentApiBackingBeanField("intValue")
        Integer getIntValue();

        @FluentApiBackingBeanField("primitiveLongValue")
        long getPrimitiveLongValue();

        @FluentApiBackingBeanField("longValue")
        Long getLongValue();

        @FluentApiBackingBeanField("primitiveFloatValue")
        float getPrimitiveFloatValue();

        @FluentApiBackingBeanField("floatValue")
        Float getFloatValue();

        @FluentApiBackingBeanField("primitiveDoubleValue")
        double getPrimitiveDoubleValue();

        @FluentApiBackingBeanField("doubleValue")
        Double getDoubleValue();

        @FluentApiBackingBeanField("enumValue")
        TestEnum getEnum();

    }


    enum TestEnum {
        ONE,
        TWO;
    }


    // Fluent Api interfaces
    @FluentApiInterface(MyRootLevelBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {


        MyRootInterface setName(@FluentApiBackingBeanMapping(value = "name") String name);

        AlternativeRootLevelInterface gotoAlternativeInterface();

        @FluentApiCommand(MyCommand.class)
        void myCommand();

        @FluentApiCommand(MyCommand.class)
        void myCommandWithParameter(@FluentApiBackingBeanMapping(value = "name") String name);

        @FluentApiCommand(MyCommand.class)
        @FluentApiImplicitValue(id = "name", value = "IT WORKS!!!")
        void myCommandWithImplicitValue();

    }

    @FluentApiInterface(MyRootLevelBackingBean.class)
    public interface AlternativeRootLevelInterface {

        @FluentApiCommand(MyCommand.class)
        void closingCommand();

    }

    @FluentApiInterface(MyMidLevelBackingBean.class)
    public interface MyMidLevelInterface {

        MyLowLevelInterface addConfig();

        MyLowLevelInterface addConfigWithPassingStringValue(@FluentApiBackingBeanMapping(value = "stringValue", target = TargetBackingBean.NEXT) String stringValue);

        @FluentApiImplicitValue(id = "stringValue", value = "IMPLICIT_PASSED", target = TargetBackingBean.NEXT)
        MyLowLevelInterface addConfigWithImplicitStringValue();

        @FluentApiBackingBeanMapping(value="midLevelBBXXX")
        MyRootInterface gotoParent();

    }

    @FluentApiInterface(MyLowLevelBackingBean.class)
    public interface MyLowLevelInterface {


        MyLowLevelInterface getStringValue(@FluentApiBackingBeanMapping("stringValue") String stringValue);

        MyLowLevelInterface setPrimitiveBooleanValue(@FluentApiBackingBeanMapping("primitiveBooleanValue") boolean booleanValue);

        MyLowLevelInterface setBooleanValue(@FluentApiBackingBeanMapping("booleanValue") Boolean booleanValue);


        MyLowLevelInterface setPrimitiveIntValue(@FluentApiBackingBeanMapping("primitiveIntValue") int intValue);


        MyLowLevelInterface setIntValue(@FluentApiBackingBeanMapping("intValue") Integer intValue);


        MyLowLevelInterface setPrimitiveLongValue(@FluentApiBackingBeanMapping("primitiveLongValue") long longValue);


        MyLowLevelInterface setLongValue(@FluentApiBackingBeanMapping("longValue") long longValue);


        MyLowLevelInterface setPrimitiveFloatValue(@FluentApiBackingBeanMapping("primitiveFloatValue") float floatValue);


        MyLowLevelInterface setFloatValue(@FluentApiBackingBeanMapping("floatValue") Float floatValue);


        MyLowLevelInterface setPrimitiveDoubleValue(@FluentApiBackingBeanMapping("primitiveDoubleValue") double doubleValue);


        MyLowLevelInterface setDoubleValue(@FluentApiBackingBeanMapping("doubleValue") Double doubleValue);


        MyLowLevelInterface setEnum(@FluentApiBackingBeanMapping("enumValue") TestEnum enumValue);

        @FluentApiImplicitValue(id = "stringValue", value = "a")
        @FluentApiImplicitValue(id = "primitiveBooleanValue", value = "true")
        @FluentApiImplicitValue(id = "booleanValue", value = "true")
        @FluentApiImplicitValue(id = "primitiveIntValue", value = "1")
        @FluentApiImplicitValue(id = "intValue", value = "2")
        @FluentApiImplicitValue(id = "primitiveLongValue", value = "3")
        @FluentApiImplicitValue(id = "longValue", value = "4")
        @FluentApiImplicitValue(id = "primitiveFloatValue", value = "1.0")
        @FluentApiImplicitValue(id = "floatValue", value = "2.0")
        @FluentApiImplicitValue(id = "primitiveDoubleValue", value = "3.0")
        @FluentApiImplicitValue(id = "doubleValue", value = "4.0")
        @FluentApiImplicitValue(id = "enumValue", value = "TWO")
        MyLowLevelInterface setImplicit();


        @FluentApiImplicitValue(id = "stringValue", value = "a")
        @FluentApiImplicitValue(id = "primitiveBooleanValue", value = "true")
        @FluentApiImplicitValue(id = "booleanValue", value = "true")
        @FluentApiImplicitValue(id = "primitiveIntValue", value = "1")
        @FluentApiImplicitValue(id = "intValue", value = "2")
        @FluentApiImplicitValue(id = "primitiveLongValue", value = "3")
        @FluentApiImplicitValue(id = "longValue", value = "4")
        @FluentApiImplicitValue(id = "primitiveFloatValue", value = "1.0")
        @FluentApiImplicitValue(id = "floatValue", value = "2.0")
        @FluentApiImplicitValue(id = "primitiveDoubleValue", value = "3.0")
        @FluentApiImplicitValue(id = "doubleValue", value = "4.0")
        @FluentApiImplicitValue(id = "enumValue", value = "TWO")
        @FluentApiBackingBeanMapping(value = "lowLevelBBs")
        MyMidLevelInterface closeWithImplicit();

        @FluentApiBackingBeanMapping(value = "lowLevelBBs")
        MyMidLevelInterface closeWithParameters(
                @FluentApiBackingBeanMapping("stringValue") String stringValue,
                @FluentApiBackingBeanMapping("primitiveBooleanValue") boolean primitiveBooleanValue,
                @FluentApiBackingBeanMapping("booleanValue") Boolean booleanValue,
                @FluentApiBackingBeanMapping("primitiveIntValue") int primitiveIntValue,
                @FluentApiBackingBeanMapping("intValue") Integer intValue,
                @FluentApiBackingBeanMapping("primitiveLongValue") long primitiveLongValue,
                @FluentApiBackingBeanMapping("longValue") long longValue,
                @FluentApiBackingBeanMapping("primitiveFloatValue") float primitiveFloatValue,
                @FluentApiBackingBeanMapping("floatValue") Float floatValue,
                @FluentApiBackingBeanMapping("primitiveDoubleValue") double primitiveDoubleValue,
                @FluentApiBackingBeanMapping("doubleValue") Double doubleValue,
                @FluentApiBackingBeanMapping("enumValue") TestEnum enumValue
        );

        @FluentApiBackingBeanMapping(value = "lowLevelBBs")
        MyMidLevelInterface closeWithoutValues();

    }

    // Commands
    @FluentApiCommand
    static class MyCommand {
        static void myCommand(MyRootLevelBackingBean backingBean) {
            System.out.println(backingBean.getName());
        }
    }


}