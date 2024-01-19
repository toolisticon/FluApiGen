package io.toolisticon.fluapigen;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;

@FluentApi("FirstApiStarter")
public class FirstApi {

    @FluentApiBackingBean
    public interface ThisSharedBB extends SharedBB.MySharedBB {

    }

    @FluentApiInterface(ThisSharedBB.class)
    @FluentApiRoot
    public interface MainInterface {

        MainInterface setName(@FluentApiBackingBeanMapping("name") String name);

        @FluentApiCommand(MyCommand.class)
        String getName();

    }


    @FluentApiCommand
    public static class MyCommand {
        public static String getName(SharedBB.MySharedBB mySharedBB) {
            return mySharedBB.name();
        }
    }

}
