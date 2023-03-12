package io.toolisticon.fluapigen;


import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import java.util.List;

@FluentApi("CuteFluentApiStarter")
public class CuteFluentApi {

    @FluentApiBackingBean
    public interface CompilerTest {

        @FluentApiBackingBeanField("testType")
        String getTestType();

        @FluentApiBackingBeanField("compilationSucceeded")
        Boolean compilationSucceeded();

        @FluentApiBackingBeanField("compileMessageChecks")
        List<CompilerMessageCheck> compilerMessageChecks();

    }

    @FluentApiBackingBean
    public interface CompilerMessageCheck {

        @FluentApiBackingBeanField("compilerMessageScope")
        CompilerMessageScope getCompilerMessageScope();

        @FluentApiBackingBeanField("compilerMessageComparisonType")
        CompilerMessageComparisonType getComparisonType();

        @FluentApiBackingBeanField("searchString")
        String getSearchString();

        @FluentApiBackingBeanField("atLine")
        Integer atLine();

    }


    public enum TestType {
        UNIT,
        BLACK_BOX
    }

    public enum CompilerMessageScope {
        NOTE,
        WARNING,
        MANDATORY_WARNING,
        ERROR;
    }

    public enum CompilerMessageComparisonType {
        CONTAINS,
        EQUALS;
    }

    @FluentApiInterface(CompilerTest.class)
    @FluentApiRoot
    public interface MyRootInterface {

        @FluentApiImplicitValue(id = "testType", value = "UNIT")
        CompilerTestInterface unitTest();


        @FluentApiImplicitValue(id = "testType", value = "BLACK_BOX")
        CompilerTestInterface blackBoxTest();


    }

    @FluentApiInterface(CompilerTest.class)
    public interface CompilerTestInterface {


        @FluentApiImplicitValue(id = "compilationSucceeded", value = "true")
        CompilerTestInterface compilationShouldSucceed();

        @FluentApiImplicitValue(id = "compilationSucceeded", value = "false")
        CompilerTestInterface compilationShouldFail();

        CompilerMessageCheckMessageType expectCompilerMessage();

        @FluentApiCommand(ExecuteTestCommand.class)
        void executeTest();

    }

    @FluentApiInterface(CompilerMessageCheck.class)
    public interface CompilerMessageCheckMessageType {

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "NOTE")
        CompilerMessageCheckComparisonType asNote();

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "WARNING")
        CompilerMessageCheckComparisonType asWarning();

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "MANDATORY_WARNING")
        CompilerMessageCheckComparisonType asMandatoryWarning();

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "ERROR")
        CompilerMessageCheckComparisonType asError();

    }

    @FluentApiInterface(CompilerMessageCheck.class)
    public interface CompilerMessageCheckComparisonType {

        @FluentApiImplicitValue(id = "compilerMessageComparisonType", value = "CONTAINS")
        @FluentApiBackingBeanMapping(value = "compileMessageChecks")
        CompilerTestInterface thatContains(@FluentApiBackingBeanMapping(value = "searchString") String text);


        @FluentApiImplicitValue(id = "compilerMessageComparisonType", value = "EQUALS")
        @FluentApiBackingBeanMapping(value = "compileMessageChecks")
        CompilerTestInterface thatEquals(@FluentApiBackingBeanMapping(value = "searchString") String text);


        CompilerMessageCheckComparisonType atLine(@FluentApiBackingBeanMapping(value = "atLine")Integer line);
    }

    // Commands
    @FluentApiCommand
    public static class ExecuteTestCommand {
        static void myCommand(CompilerTest backingBean) {
            System.out.println("DONE");
        }
    }


}