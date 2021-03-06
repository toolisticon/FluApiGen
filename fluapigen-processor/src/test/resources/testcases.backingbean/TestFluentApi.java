package io.toolisticon.fluapigen;


import io.toolisticon.fluapigen.api.BackingBeanFieldRef;
import io.toolisticon.fluapigen.api.BackingBean;
import io.toolisticon.fluapigen.api.BackingBeanField;
import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.SetImplicitValue;

import java.util.List;

@FluentApi(
        rootInterface = TestFluentApi.ExampleRoot.class,
        builderClassName = "TestFluentApiBuilder"
)
public class TestFluentApi {

    @BackingBean
    public interface RootBB {
        String getName();

        Integer getAge();

        List<MessageCheckBB> getMessageChecks();
    }

    @BackingBean
    public interface MessageCheckBB {
        String atSourceFile();

        Long atLine();

        Long atColumn();

        String getEqualComparisonString();

        String[] containsStrings();
    }

    @FluentApiInterface(RootBB.class)
    public interface ExampleRoot {

        @BackingBeanField("name")
        ExampleRoot setName(String name);

        @BackingBeanField("age")
        ExampleRoot setAge(Integer age);

        @BackingBeanFieldRef("checkType")
        @SetImplicitValue("WARN")
        MessageCheck error();

        @BackingBeanFieldRef("checkType")
        @SetImplicitValue("INFO")
        MessageCheck info();

        @BackingBeanFieldRef("checkType")
        @SetImplicitValue("ERROR")
        MessageCheck warning();

    }

    public enum MessageCheckType {
        WARN,
        INFO,
        ERROR;
    }

    @FluentApiInterface(MessageCheckBB.class)
    public interface MessageCheck {

        MessageCheck atSourceFile(String source);

        MessageCheck atLine(Long line);

        MessageCheck atColumn(Long column);

        ExampleRoot isEqual(String message);

        ExampleRoot contains(String... token);

    }


}
