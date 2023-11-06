package io.toolisticon.cute;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;

import java.util.ArrayList;
import java.util.List;

@FluentApi("InheritenceTestBuilder")
public class GenericsTest {


    @FluentApiBackingBean
    public interface MyRootBackingBean {
        List<?> values();

        Conversion filterBy();
    }


    enum Conversion {
        STRING,
        INTEGER
    }


    @FluentApiRoot
    @FluentApiInterface(MyRootBackingBean.class)
    public interface RootInterface {

        MyFilterInterface valuesToFilter(@FluentApiBackingBeanMapping(value = "values", action = MappingAction.SET) List<?> values);
    }

    @FluentApiInterface(MyRootBackingBean.class)
    public interface MyFilterInterface {
        @FluentApiImplicitValue(id = "filterBy", value = "STRING")
        MyClosingInterface<String> isString();

        @FluentApiImplicitValue(id = "filterBy", value = "INTEGER")
        MyClosingInterface<Integer> isInteger();
    }


    @FluentApiInterface(MyRootBackingBean.class)
    public interface MyClosingInterface<T> {

        @FluentApiCommand(ClosingCommand.class)
        List<T> close();

    }


    @FluentApiCommand
    public static class ClosingCommand {
        static <T> List<T> myCommand(GenericsTest.MyRootBackingBean backingBean) {
            List<T> result = new ArrayList<>();

            for (Object value : backingBean.values()) {
                switch (backingBean.filterBy()) {
                    case STRING: {
                        if (value instanceof String) {
                            result.add((T) value);
                        }
                        break;
                    }
                    case INTEGER: {
                        if (value instanceof Integer) {
                            result.add((T) value);
                        }
                        break;
                    }
                    default: {
                        result.add((T) value);
                    }
                }
            }

            return (List<T>) result;
        }
    }
}
