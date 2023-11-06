package io.toolisticon.fluapigen.testcases;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiConverter;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import java.util.List;

@FluentApi("IntegrationTestStarter")
public class IntegrationTest_BackingBeanMapping_Converter_WrongTargetArrayValue {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyRootLevelBackingBean {

        @FluentApiBackingBeanField("singleValue")
        String getSingleValue();

        @FluentApiBackingBeanField("arrayValue")
        String[] getArrayValue();

        @FluentApiBackingBeanField("collectionValue")
        List<String> getCollectionValue();


    }

    public static class MySingleValueConverter implements FluentApiConverter<Long, String> {
        @Override
        public String convert(Long aLong) {
            // ignore null safety..
            return aLong.toString();
        }
    }

    public static class MyArrayValueConverter implements FluentApiConverter<Long[], Long[]> {
        @Override
        public Long[] convert(Long[] aLong) {
            // ignore null safety..
            return null;
        }
    }

    public static class MyCollectionValueConverter implements FluentApiConverter<List<Long>, List<String>> {
        @Override
        public List<String> convert(List<Long> aLong) {
            // ignore null safety..
            return null;
        }
    }

    // Fluent Api interfaces
    @FluentApiInterface(MyRootLevelBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        MyRootInterface setSingleValue(@FluentApiBackingBeanMapping(value = "singleValue", converter = MySingleValueConverter.class) Long name);

        MyRootInterface setArrayValue(@FluentApiBackingBeanMapping(value = "arrayValue", converter = MyArrayValueConverter.class) Long[] name);

        MyRootInterface setCollectionValue(@FluentApiBackingBeanMapping(value = "collectionValue", converter = MyCollectionValueConverter.class) List<Long> name);


        @FluentApiCommand(MyCommand.class)
        void myCommand();


    }

    // Commands
    @FluentApiCommand
    static class MyCommand {
        static void myCommand(MyRootLevelBackingBean backingBean) {
            System.out.println(backingBean.getSingleValue());
        }
    }


}