package io.toolisticon.cute;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiConverter;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;

import java.util.List;

@FluentApi("ImplicitValueConverterTestBuilder")
public class IntegrationTest_ImplicitValueConverterTest_withInvalidConverter_invalidSourceType {

    public static class TargetType {

        private final String value;

        public TargetType (String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static class MyConverter implements FluentApiConverter<Integer,TargetType> {

        @Override
        public TargetType convert(Integer o) {
            return new TargetType(o.toString());
        }

    }

    @FluentApiBackingBean
    public interface TestBackingBean {

        TargetType singleValue();

        List<TargetType> collectionValue();

        TargetType[] arrayValue();

    }

    @FluentApiRoot
    @FluentApiInterface(value = TestBackingBean.class)
    public interface MyFluentInterface{

        @FluentApiImplicitValue(id="singleValue", value = "SINGLE", converter = MyConverter.class)
        MyFluentInterface setSingleValue();

        @FluentApiImplicitValue(id = "collectionValue", value={"ONE", "TWO", "THREE"}, action = MappingAction.SET, converter = MyConverter.class)
        MyFluentInterface setCollectionValue();

        @FluentApiImplicitValue(id = "collectionValue", value={"A", "B", "C"}, action = MappingAction.ADD, converter = MyConverter.class)
        MyFluentInterface addCollectionValue();

        @FluentApiImplicitValue(id="arrayValue", value = {"A", "B", "C"},converter = MyConverter.class)
        MyFluentInterface setArrayValue();

        @FluentApiCommand(MyCommand.class)
        TestBackingBean myCommand();

    }

    @FluentApiCommand()
    public static class MyCommand {
        public static TestBackingBean myCommand(TestBackingBean testBackingBean) {
            return testBackingBean;
        }
    }

}
