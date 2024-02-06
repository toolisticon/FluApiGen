# FLUAPIGEN - The Fluent API Generator

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.fluapigen/fluapigen/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.toolisticon.fluapigen/fluapigen)
[![default](https://github.com/toolisticon/FluApiGen/actions/workflows/default.yml/badge.svg)](https://github.com/toolisticon/FluApiGen/actions/workflows/default.yml)
[![release_on_master](https://github.com/toolisticon/FluApiGen/actions/workflows/release.yml/badge.svg?branch=master)](https://github.com/toolisticon/FluApiGen/actions/workflows/release.yml)
[![codecov](https://codecov.io/gh/toolisticon/FluApiGen/branch/develop/graph/badge.svg?token=FlcugFxC64)](https://codecov.io/gh/toolisticon/FluApiGen)
[![javadoc](https://javadoc.io/badge2/io.toolisticon.fluapigen/fluapigen-api/javadoc.svg)](https://javadoc.io/doc/io.toolisticon.fluapigen/fluapigen-api)

Implementing and especially maintaining of fluent and immutable apis is one of a most annoying and difficult tasks to do in java developing.

You usually have to implement a lot of boilerplate code that is only needed to handle and clone the fluent apis internal state.
Refactoring of an existing fluent api can therefore be a very complex thing to do.

This project provides an annotation processor that generates fluent api implementations and therefore completely hiding all necessary boilerplate code.
To achieve this all that needs to be done is to define some fluent and backing bean interfaces and command classes and to configure its "plumbing" by placing a few annotations.
.

# Features
- fluent api is created by defining some interfaces and placing some annotations on them
- this projects annotation processor then generates the fluent api implementation for you
- it's possible to use converters and validators on fluent api parameters
- implementing, extending and maintaining of an immutable, fluent api becomes a no-brainer

# Restrictions
- fluent interfaces must not contain any cycles and must be strongly hierarchically

# How does it work?

## Project Setup

The api lib must be bound as a dependency - for example in maven:
```xml
<dependencies>

    <dependency>
        <groupId>io.toolisticon.fluapigen</groupId>
        <artifactId>fluapigen-api</artifactId>
        <version>0.8.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- optional - add it if you want to use validators -->
    <dependency>
        <groupId>io.toolisticon.fluapigen</groupId>
        <artifactId>fluapigen-validation-api</artifactId>
        <version>0.8.0</version>
        <scope>compile</scope>
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
                <version>0.8.0</version>
            </path>
        </annotationProcessorPaths>
        
    </configuration>

</plugin>
```

## Documentation
Basically you have to create a class annotated with the _FluentApi_ annotation which takes the fluent apis base class name as attribute.

```java
@FluentApi("CuteFluentApiStarter")
public class CuteFluentApi {
    // Contains the interfaces for 
    // backing beans 
    // and fluent api 
    // and classes for closing commands.
}
```

The fluent api can be defined inside that class by using three kinds of components:
- the fluent api interfaces
- the backing bean interfaces which are storing the configuration data passed in via the fluent api interfaces
- command classes which are taking the root backing bean as a parameter and doing things with it

The following diagram demonstrates how those components relate to each other:

![Fluapigen Setup](/documentation/Fluapigen.png)

The development of the fluent api can be broke down to 3 different steps.

### Defining The Backing Bean
The backing bean interfaces are defining the configuration (or in other word context) to be build.
It basically defines the getter methods for all values used by the fluent api.
Therefore, all methods must have a non-void return type and must not have any parameters.

The backing bean interfaces must be annotated with the _FluentApiBackingBean_ annotation.
The value getter methods may be annotated with the _FluentApiBackingBeanField_ annotation.
By doing this it's possible to declare an id for the field and its initial value.
If the id is not explicitly it will use the fields method name as a fallback.
Field ids must be unique in the interface.

```java
@FluentApiBackingBean
public interface CompilerTest {

    // annotation is optional - ids default to method name
    @FluentApiBackingBeanField("value1")
    String getValue1();

    // embedded Backing Bean
    @FluentApiBackingBeanField("embeddedBackingBean")
    List<CompilerMessageCheck> compilerMessageChecks();

}
```

Child backing beans can be defined as single values or as Collection types of List or Set.

### Defining Closing Commands
Closing commands can be defined by defining static inner classes annotated with the _FluentApiCommand_ annotation.
A command class must contain exactly one static method which takes the backing bean as the only parameter.

It's later possible to link a fluent api method to a closing command.
The fluent api method call then will be forwarded to the closing command.

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
Each interface will be bound to one specific backing bean. A backing bean can be bound by multiple fluent interfaces.

Methods defined in those interfaces are only allowed to return other fluent api interfaces, except for closing commands.

All method parameters and methods that close a backing bean creation must be annotated with the _FluentApiBackingBeanField_ annotation which defines which value should be written.
Of course parameter and value type must match or a correct converter must be used. 

Default methods will completely be ignored by the processor and can be used to transform parameters and to delegate calls to another interface method. 

There must be exactly one interface annotated with the _FluentApiRoot_ annotation. 
Those interfaces methods will be available as static starter methods for the fluent api.

Please check the following example for further information - because the code explains itself pretty well.


## Example

This is a small example related to the [toolisticon CUTE]() project.
CUTE a compile testing framework for testing annotation processors that allows you to configure test by using an immutable fluent api.
In this example checks for compiler outcome and for specific compiler messages can be defined.

```java
package io.toolisticon.fluapigen.integrationtest;


import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;

import java.util.List;

@FluentApi("CuteFluentApiStarter")
public class CuteFluentApi {

    @FluentApiBackingBean
    public interface CompilerTest {

        String testType();

        Boolean compilationSucceeded();

        List<CompilerMessageCheck> compilerMessageChecks();

    }

    @FluentApiBackingBean
    public interface CompilerMessageCheck {

        CompilerMessageScope compilerMessageScope();

        CompilerMessageComparisonType compilerMessageComparisonType();

        String searchString();

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
        @FluentApiParentBackingBeanMapping(value = "compileMessageChecks")
        CompilerTestInterface thatContains(@FluentApiBackingBeanMapping(value = "searchString") String text);
        
        @FluentApiImplicitValue(id = "compilerMessageComparisonType", value = "EQUALS")
        @FluentApiParentBackingBeanMapping(value = "compileMessageChecks")
        CompilerTestInterface thatEquals(@FluentApiBackingBeanMapping(value = "searchString") String text);
        
        CompilerMessageCheckComparisonType atLine(@FluentApiBackingBeanMapping(value = "atLine")Integer line);
    }

    // Commands
    @FluentApiCommand
    public static class ExecuteTestCommand {
        static void myCommand(CompilerTest backingBean) {
            /// ...
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

## Advanced Techniques

### Inline Backing Bean Mappings
Sometimes you have smaller backing beans which you might want to declare inline by using multiple parameters of the fluent api method.
The fluent api generator provides you the possibility to do that.
You have to add the _FluentApiInlineBackingBeanMapping_ annotation to the method and set the target of the corresponding _FluentApiBackingBeanMapping_ to INLINE.

```java
@FluentApiInterface(value = MyBackingBean.class)
public interface MyFluentInterface {
    
    // Converters in backing bean mappings
    @FluentApiInlineBackingBeanMapping("nameOfTargetBackingBeanField")
    MyFluentInterface doSomething(
            @FluentApiBackingBeanMapping(value = "value1", target=TargetBackingBean.INLINE) Long value1,
            @FluentApiBackingBeanMapping(value = "value2", target=TargetBackingBean.INLINE) Long value2
    );

}
```
In this example an inline backing bean will be set at _MyBackingBean.nameOfTargetBackingBeanField_.
It's _value1_ and _value2_ attributes will be mapped from the corresponding method parameters.

### Converters
Converters can be used to map input parameters from either implicit value annotations or backing bean mappings to the backing bean type.

An example Converter:
```java
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

public static class MyStringConverter implements FluentApiConverter<String,TargetType> {

    @Override
    public TargetType convert(String o) {
        return new TargetType(o);
    }

}

public static class MyLongConverter implements FluentApiConverter<String, TargetType> {

    @Override
    public TargetType convert(Long o) {
        // not null save ;)
        return new TargetType(o.toString());
    }

}

@FluentApiInterface(value = MyBackingBean.class)
public interface MyFluentInterface {
    
    // Converters in implicit value annotation
    @FluentApiImplicitValue(id = "singleValue", value = "SINGLE", converter = MyStringConverter.class)
    MyFluentInterface setSingleValue();
    
    // Converters in backing bean mappings
    MyFluentInterface setViaLongValue(@FluentApiBackingBeanMapping(value = "singleValue", converter = MyLongConverter.class) Long longValue);

}
```
### Using validators at fluent interface method parameters
FluApiGen provides some basic validators that can be applied to the fluent method parameters at runtime.

- _Matches_ : Regular Expression validator for Strings
- _MinLength / MaxLength_ : Checks length of Strings or size of Arrays or Collections
- _NotEmpty_ : Checks if String, array and Collection are not empty
- _NotNull_ : Checks if passed argument isn't null

To be able to use validators, the _fluapigen_validation_api_ library must be linked at compile and runtime.

````java
MyRootInterface setName(@Matches("Max.*") @FluentApiBackingBeanMapping("name") String name);
````

In this example the fluent api would throw a _ValidatorException_ if the name doesn't start with "Max".

It's possible to use multiple validators on a parameter and even to provide custom validators:

````java
@FluentApiValidator(value = Matches.ValidatorImpl.class, parameterNames = {"value"})
public @interface Matches {

    String value();

    class ValidatorImpl implements Validator<String> {

        private final String regularExpression;

        public ValidatorImpl(String regularExpression) {
            this.regularExpression = regularExpression;
        }

        @Override
        public boolean validate(String obj) {
            return Pattern.compile(regularExpression).matcher(obj).matches();
        }

    }

}
````

Validators are annotations annotated with _FluentApiValidator_ meta annotation. The _FluentApiValidator_ annotation is used to reference the validators implementation class that must implement the _Validator_ interface.
Validation criteria can be added as annotation attributes. The _FluentApiValidator_ meta annotation defines the attribute to validator constructor mapping via the parameterNames attribute.
The validator implementation must provide a matching constructor.

*Remark : The feature is currently under development and not 100% done. There will still be improvements regarding processing time validation and error output*

### Javas default methods in fluent api and backing bean in interfaces
Default methods will be ignored during processing of fluent api and backing bean interfaces and can be used for different tasks:

- They can provide alternative ways to set a parameter by providing conversions of input parameters and internally calling fluent api methods.
- They can also be very helpful to provide methods for accessing/filtering/aggregate backing bean attributes.
 
### Using parent interfaces to share method declarations and backing bean fields declarations
It's possible to use parent interfaces to share fluent api method declarations in multiple interfaces:

```java
    @FluentApiBackingBean
    interface MyRootLevelBackingBean {

        @FluentApiBackingBeanField("1st")
        FirstInheritedBackingBean get1st();

        @FluentApiBackingBeanField("2nd")
        SecondInheritedBackingBean get2nd();

    }


    // Parent backing bean interface must not be annotated with FluentApiBackingBean annotation
    interface MyReusedBackingBeanFields {

        @FluentApiBackingBeanField("name")
        String getName();

    }


    @FluentApiBackingBean
    interface FirstInheritedBackingBean extends MyReusedBackingBeanFields {

        @FluentApiBackingBeanField("1st")
        String get1st();

    }

    @FluentApiBackingBean
    interface SecondInheritedBackingBean extends MyReusedBackingBeanFields {

        @FluentApiBackingBeanField("2nd")
        String get2nd();

    }


    // Fluent Api interfaces
    @FluentApiInterface(MyRootLevelBackingBean.class)
    @FluentApiRoot
    public interface MyRootInterface {

        My1stInterface goto1st();

        My2ndInterface goto2nd();

        @FluentApiCommand(MyCommand.class)
        void myCommand();


    }

    // Parent fluent api interface must not be annotated with FluentApiInterface
    // There must be a related backing bean interface for related attributes
    // Type Parameters must be used to pass in the followup fluent api interfaces
    public interface SharedInterface<FLUENT_INTERFACE> {

        // Fluebt followup interface must be returned as type variable
        FLUENT_INTERFACE setName(@FluentApiBackingBeanMapping(value = "name") String name);

    }

    
    @FluentApiInterface(FirstInheritedBackingBean.class)
    public interface My1stInterface extends SharedInterface<My1stInterface> {

        @FluentApiParentBackingBeanMapping("1st")
        MyRootInterface set1st(@FluentApiBackingBeanMapping(value = "1st") String first);

    }

    @FluentApiInterface(SecondInheritedBackingBean.class)
    public interface My2ndInterface extends SharedInterface<My2ndInterface> {

        @FluentApiParentBackingBeanMapping("2nd")
        MyRootInterface set1st(@FluentApiBackingBeanMapping(value = "2nd") String second);

    }

    // Commands
    @FluentApiCommand
    static class MyCommand {
        static void myCommand(MyRootLevelBackingBean backingBean) {
            System.out.println("CHECK");
        }
    }
```


# Contributing

We welcome any kind of suggestions and pull requests.

## Building and developing the FluApiGen annotation processor

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
