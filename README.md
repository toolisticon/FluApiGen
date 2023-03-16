# FLUAPIGEN - The Fluent API Generator

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.fluapigen/fluapigen/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.fluapigen/fluapigen)
![Github Actions Build](https://github.com/toolisticon/FluApiGen/actions/workflows/default.yml/badge.svg)
![Github Actions Release](https://github.com/toolisticon/FluApiGen/actions/workflows/release.yml/badge.svg)
[![codecov](https://codecov.io/gh/toolisticon/FluApiGen/branch/develop/graph/badge.svg?token=FlcugFxC64)](https://codecov.io/gh/toolisticon/FluApiGen)

Implementing and maintaining of fluent, immutable apis is one of a most annoying and difficult task in java developing.

This project helps you to create fluent apis just by defining some interfaces and annotating them.

Then this annotation processor will generate an implementation for you - by doing this it completely hides all necessary boilerplate code from you. 

# Features
- fluent api is created by defining some interfaces and placing some annotations on them
- our annotation processor then generates the fluent api implementation for you
- implementing, extending and maintaining of an immutable, fluent api becomes a no-brainer

# Restrictions
- interfaces must not contain any cycles
- there will be some extensions in the near future to allow you to do mapping by for example parameter name, reducing the amount of annotations needed to build the api

# How does it work?

## Project Setup
A release will be done in the next couple of days.
A description about how to bind dependencies will be added afterwards

The api lib must be bound as a dependency - for example in maven:
```xml
<dependencies>

    <dependency>
        <groupId>io.toolisticon.fluapigen</groupId>
        <artifactId>fluapigen-api</artifactId>
        <version>0.1.0</version>
    </dependency>

</dependencies>
```

Additionally, you need to declare the annotation processor path in your compiler plugin:

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>

    <configuration>
        
        <annotationProcessorPaths>
            <path>
                <groupId>io.toolisticon.fluapigen</groupId>
                <artifactId>fluapigen-processor</artifactId>
                <version>0.1.0</version>
            </path>
        </annotationProcessorPaths>
        
    </configuration>

</plugin>
```

## Documentation
Basically you have to create a class annotated with the _FluentApi_ annotation which has the fluent apis base class name as attribute.

Then you are able to define your api inside this class via interfaces.

```java
@FluentApi("CuteFluentApiStarter")
public class CuteFluentApi {
    // Your interfaces for backing beans and fluent api and classes for closing commands.
}
```

This can be broke down to 3 different steps.

### Defining The Backing Bean
The backing bean interfaces are defining the configuration to be build.
It basically defines the getter methods for are values set by the fluent api.
Therefor all methods must have a non void return type and must not have any parameters.

The backing bean interfaces must be annotated with the _FluentApiBackingBean_ anotation.
The value getter methods must be annotated with the _FluentApiBackingBeanField_ annotation and must have an interface unique id.

```java
@FluentApiBackingBean
public interface CompilerTest {

    @FluentApiBackingBeanField("value1")
    String getValue1();

    // embedded Backing Bean
    @FluentApiBackingBeanField("embeddedBackingBean")
    List<CompilerMessageCheck> compilerMessageChecks();

}
```

Child backing beans can be included as single value or List or Set.

### Defining Closing Commands
Closing commands can be defined by defining static inner classes annotated with the _FluentApiCommand_ annotation.
A command class must contain exactly one static method which takes the backing bean as the only parameter.


It's later possible to link a fluent api method to a closing command.
The fluent api method call will then be forwarded to the closing command.

```java
@FluentApiCommand
public static class ExecuteTestCommand {
    public static void myCommand(CompilerTest backingBean) {
        // do something
    }
}
```

Commands may have a return value.

### Defining The Fluent Api

The fluent api must be defined in multiple interfaces annotated with the _FluentApiInterface_ annotation.
Those interface will be bound to one specific backing bean. A backing bean can be bound by multiple fluent interfaces.

Methods defined in those interfaces are only allowed to return other fluent api interfaces, except for closing commands.

All method parameters and methods that close a backing bean creation must be annotated with the _FluentApiBackingBeanField_ annotation which defines which value should be written.
Of course parameter and value type must match.

There must be exactly one interface annotated with the _FluentApiRoot_ annotation. 
Those interfaces methods will be available as static starter methods for the fluent api.

Please check the following example for further information - because the code explains itself pretty well.

## Example

This is a small example related to the [toolisticon CUTE]() project.
It's a compile testing framework that allows you to configure test by using a fluent api.
In this example checks for compiler outcome and for specific compiler messages can be defined.

```java
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
            // Do something by using the backing bean
            // ...
        }
    }


}
```

The fluent api can then be used as followed:

```java
CuteFluentApiStarter.unitTest()
    .compilationShouldSucceed()
    .expectCompilerMessage().asError().atLine(10).thatContains("ABC")
    .expectCompilerMessage().asError().thatContains("DEF")
    .executeTest();
```
    
# Contributing

We welcome any kind of suggestions and pull requests.

## Building and developing the ${rootArtifactId} annotation processor

This project is built using Maven.
A simple import of the pom in your IDE should get you up and running. To build the project on the commandline, just run `./mvnw` or `./mvnw clean install`

## Requirements

The likelihood of a pull request being used rises with the following properties:

- You have used a feature branch.
- You have included a test that demonstrates the functionality added or fixed.
- You adhered to the [code conventions](http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html).

# License

This project is released under the revised [MIT License](LICENSE).

This project includes and repackages the [Annotation-Processor-Toolkit](https://github.com/holisticon/annotation-processor-toolkit) released under the  [MIT License](/3rdPartyLicenses/annotation-processor-toolkit/LICENSE.txt).
