package io.toolisticon.fluapigen;


import io.toolisticon.fluapigen.api.BackingBeanFieldRef;
import io.toolisticon.fluapigen.api.BackingBean;
import io.toolisticon.fluapigen.api.FinishingCommand;
import io.toolisticon.fluapigen.api.FinishingCommandId;
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
    public interface BackingBeanRoot {
        String getName();

        Integer getAge();

        List<MessageCheck> getMessageChecks();
    }

    @BackingBean
    public interface MessageCheckBB {
        String getAtSourceFile();

        Long getAtLine();

        Long getAtColumn();

        String getIsEqual();

        String[] contains();
    }


    @FluentApiInterface(BackingBeanRoot.class)
    public interface ExampleRoot {

        @BackingBeanFieldRef("name")
        ExampleRoot setName(String name);

        @BackingBeanFieldRef("age")
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

        @FinishingCommand(TestFluentApi.class)
        String finishIt();

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

    @FinishingCommandId()
    public static String doThat(BackingBeanRoot config) {
        return "yes";
    }

}
