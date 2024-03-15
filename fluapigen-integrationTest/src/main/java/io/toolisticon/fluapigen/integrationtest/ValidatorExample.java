package io.toolisticon.fluapigen.integrationtest;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;
import io.toolisticon.fluapigen.validation.api.HasNoArgConstructor;
import io.toolisticon.fluapigen.validation.api.Matches;
import io.toolisticon.fluapigen.validation.api.MaxLength;
import io.toolisticon.fluapigen.validation.api.NotNull;
import io.toolisticon.fluapigen.validation.api.Nullable;

import java.util.List;

@FluentApi("ValidatorExampleStarter")
public class ValidatorExample {

    // Backing Bean Interface
    @FluentApiBackingBean
    interface MyBackingBean {

        @FluentApiBackingBeanField("name")
        String getName();

        @FluentApiBackingBeanField("variousNames")
        List<String> getVariousNames();

        @FluentApiBackingBeanField("classes")
        List<Class<?>> getClasses();

    }

    // Fluent Api interfaces
    @FluentApiInterface(MyBackingBean.class)
    @FluentApiRoot
    @Nullable
    public interface MyRootInterface {

        MyRootInterface setName(@NotNull @MaxLength(8) @Matches("aaa.*") @FluentApiBackingBeanMapping("name") String name);

        @NotNull
        MyRootInterface setNameWithNullableOnParameter(@Nullable @MaxLength(8) @Matches("aaa.*") @FluentApiBackingBeanMapping("name") String name);

        @Nullable
        MyRootInterface setNameWithNullableOnMethod(@NotNull @MaxLength(8) @Matches("aaa.*") @FluentApiBackingBeanMapping("name") String name);

        @Nullable
        MyRootInterface setVariousNamesWithNullableOnMethod(@NotNull @MaxLength(8) @Matches("aaa.*") @FluentApiBackingBeanMapping("variousNames") String... names);

        @Nullable
        MyRootInterface setVariousNamesWithNullableOnMethod(@NotNull @MaxLength(8) @Matches("aaa.*") @FluentApiBackingBeanMapping("variousNames") List<String> names);

        MyRootInterface addClass(@NotNull @HasNoArgConstructor @FluentApiBackingBeanMapping(value = "classes", action = MappingAction.ADD) Class<?> clazz);

        MyRootInterface addClasses(@NotNull @HasNoArgConstructor @FluentApiBackingBeanMapping(value = "classes", action = MappingAction.ADD) Class<?>... clazz);

        MyRootInterface addClasses(@NotNull @HasNoArgConstructor @FluentApiBackingBeanMapping(value = "classes", action = MappingAction.ADD) Iterable<Class<?>> clazz);

        @FluentApiCommand(MyCommand.class)
        void myCommand();

    }

    // Commands
    @FluentApiCommand
    static class MyCommand {
        static void myCommand(MyBackingBean backingBean) {
            System.out.println(backingBean.getName());
            System.out.println(backingBean.getVariousNames());
        }
    }


}