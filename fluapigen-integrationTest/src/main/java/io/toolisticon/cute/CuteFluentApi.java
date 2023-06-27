package io.toolisticon.cute;


import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;
import io.toolisticon.fluapigen.api.TargetBackingBean;

import javax.annotation.processing.Processor;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Locale;

@FluentApi("CuteFluentApiStarter")
public class CuteFluentApi {

    @FluentApiBackingBean
    public interface CompilerTestBB {

        @FluentApiBackingBeanField("testType")
        String getTestType();



        @FluentApiBackingBeanField("compilationSucceeded")
        Boolean compilationSucceeded();

        @FluentApiBackingBeanField("exceptionIsThrown")
        Class<? extends Exception> getExceptionIsThrown();

        @FluentApiBackingBeanField("compileMessageChecks")
        List<CompilerMessageCheckBB> compilerMessageChecks();

        @FluentApiBackingBeanField("javaFileObjectChecks")
        List<GeneratedJavaFileObjectCheckBB> javaFileObjectChecks();

        @FluentApiBackingBeanField("fileObjectChecks")
        List<GeneratedFileObjectCheckBB> fileObjectChecks();

    }


    @FluentApiBackingBean
    public interface PassInConfigurationBB {
        @FluentApiBackingBeanField("passedInClass")
         Class<?> getPassedInClass();

        @FluentApiBackingBeanField("annotationToScanFor")
         Class<? extends Annotation> getAnnotationToScanFor();

        @FluentApiBackingBeanField("passedInProcessor")
        Class<? extends Processor> getPassedInProcessor();
    }

    @FluentApiBackingBean
    public interface CompilerMessageCheckBB {

        @FluentApiBackingBeanField("compilerMessageScope")
        CompilerMessageScope getCompilerMessageScope();

        @FluentApiBackingBeanField("compilerMessageComparisonType")
        CompilerMessageComparisonType getComparisonType();

        @FluentApiBackingBeanField("searchString")
        List<String> getSearchString();

        @FluentApiBackingBeanField("atLine")
        Integer atLine();

        @FluentApiBackingBeanField("atColumn")
        Integer atColumn();

        @FluentApiBackingBeanField("atSource")
        String atSource();

        @FluentApiBackingBeanField("withLocale")
        Locale withLocale();

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


    public enum FileObjectCheckType {
        EXISTS,
        DOESNT_EXIST
    }

    @FluentApiBackingBean
    public interface GeneratedJavaFileObjectCheckBB {

        @FluentApiBackingBeanField("checkType")
        FileObjectCheckType getCheckType();

        @FluentApiBackingBeanField("location")
        JavaFileManager.Location getLocation();

        @FluentApiBackingBeanField("className")
        String getClassName();

        @FluentApiBackingBeanField("kind")
        JavaFileObject.Kind getKind();

        @FluentApiBackingBeanField("generatedFileObjectMatcher")
        GeneratedFileObjectMatcher getGeneratedFileObjectMatcher();
    }

    @FluentApiBackingBean
    public interface GeneratedFileObjectCheckBB {

        @FluentApiBackingBeanField("checkType")
        FileObjectCheckType getCheckType();

        @FluentApiBackingBeanField("location")
        JavaFileManager.Location getLocation();

        @FluentApiBackingBeanField("packageName")
        String getPackageName();

        @FluentApiBackingBeanField("relativeName")
        String getRelativeName();

        @FluentApiBackingBeanField("generatedFileObjectMatcher")
        GeneratedFileObjectMatcher[] getGeneratedFileObjectMatchers();
    }


    @FluentApiInterface(CompilerTestBB.class)
    @FluentApiRoot
    public interface MyRootInterface {

        @FluentApiImplicitValue(id = "testType", value = "UNIT")
        CompilerTestInterface unitTest();


        @FluentApiImplicitValue(id = "testType", value = "BLACK_BOX")
        CompilerTestInterface blackBoxTest();


    }

    @FluentApiInterface(CompilerTestBB.class)
    public interface UnitTestRootInterface {


        CompilerTestInterface when();

    }

    @FluentApiInterface(CompilerTestBB.class)
    public interface UnitTestGivenInterface {

        UnitTestWhenInterface when();

    }

    @FluentApiInterface(PassInConfigurationBB.class)
    public interface UnitTestWhenInterface {

        default <E extends Element> UnitTestWhenWithPassedInElementInterface<E> passInElement(Class<?> classToScan){
            return this.<E>passInElement(classToScan, PassIn.class);
        }

        <E extends Element> UnitTestWhenWithPassedInElementInterface<E> passInElement(@FluentApiBackingBeanMapping(value = "passedInClass") Class<?> classToScan, @FluentApiBackingBeanMapping(value = "annotationToScanFor") Class<? extends Annotation> annotationToSearch);

        <P extends Processor> UnitTestWhenWithPassedInProcessorInterface<P> passInProcessor(@FluentApiBackingBeanMapping(value = "passedInProcessor")Class<P> processor);

        default <E extends Element, P extends Processor> UnitTestWhenWithPassedInElementAndProcessorInterface<E,P> passInElementAndProcessor(Class<?> classToScanForElement, Class<P> processor){
            return this.<E,P>passInElementAndProcessor(classToScanForElement, PassIn.class, processor);
        }

        <E extends Element, P extends Processor> UnitTestWhenWithPassedInElementAndProcessorInterface<E,P> passInElementAndProcessor(@FluentApiBackingBeanMapping(value = "passedInClass")Class<?> classToScanForElement, @FluentApiBackingBeanMapping(value = "annotationToScanFor") Class<? extends Annotation> annotationToSearch, @FluentApiBackingBeanMapping(value = "passedInProcessor")Class<P> processor);


    }

    @FluentApiInterface(CompilerTestBB.class)
    public interface UnitTestWhenWithPassedInElementInterface <E extends Element>{
        //CompilerTestInterface into (@FluentApiBackingBeanMapping() UnitTestAnnotationProcessorClassWithPassIn<E> unitTest);
    }

    @FluentApiInterface(CompilerTestBB.class)
    public interface UnitTestWhenWithPassedInProcessorInterface <P extends Processor>{
        //CompilerTestInterface into (UnitTestAnnotationProcessorClassForTestingAnnotationProcessors<P, Element> unitTest);
    }

    @FluentApiInterface(CompilerTestBB.class)
    public interface UnitTestWhenWithPassedInElementAndProcessorInterface <E extends Element, P extends Processor>{
        //CompilerTestInterface into (UnitTestAnnotationProcessorClassForTestingAnnotationProcessorsWithPassIn<P, E> unitTest);
    }

    @FluentApiInterface(CompilerTestBB.class)
    public interface CompilerTestInterface {


        CompilerTestExpectThatInterface expectThat();


    }






    @FluentApiInterface(CompilerTestBB.class)
    public interface CompilerTestExpectAndThatInterface {

        CompilerTestExpectThatInterface andThat();

        @FluentApiCommand(ExecuteTestCommand.class)
        void executeTest();

    }

    @FluentApiInterface(CompilerTestBB.class)
    public interface CompilerTestExpectThatInterface {

        @FluentApiImplicitValue(id = "compilationSucceeded", value = "true")
        CompilerTestExpectAndThatInterface compilationSucceeds();

        @FluentApiImplicitValue(id = "compilationSucceeded", value = "false")
        CompilerTestExpectAndThatInterface compilationFails();


        default GeneratedJavaFileObjectCheck generatedClass(String className) {
            return javaFileObject(StandardLocation.CLASS_OUTPUT, className, JavaFileObject.Kind.CLASS);
        }

        default GeneratedJavaFileObjectCheck generatedSourceFile(String className) {
            return javaFileObject(StandardLocation.CLASS_OUTPUT, className, JavaFileObject.Kind.SOURCE);
        }

        default GeneratedFileObjectCheck generatedResourceFile(String packageName,
                                                               String relativeName) {
            return fileObject(StandardLocation.CLASS_OUTPUT, packageName, relativeName);
        }

        GeneratedJavaFileObjectCheck javaFileObject(
                @FluentApiBackingBeanMapping(value = "location", target = TargetBackingBean.NEXT) JavaFileManager.Location location,
                @FluentApiBackingBeanMapping(value = "className", target = TargetBackingBean.NEXT) String className,
                @FluentApiBackingBeanMapping(value = "kind", target = TargetBackingBean.NEXT) JavaFileObject.Kind kind);

        GeneratedFileObjectCheck fileObject(
                @FluentApiBackingBeanMapping(value = "location", target = TargetBackingBean.NEXT) JavaFileManager.Location location,
                @FluentApiBackingBeanMapping(value = "packageName", target = TargetBackingBean.NEXT) String packageName,
                @FluentApiBackingBeanMapping(value = "relativeName", target = TargetBackingBean.NEXT) String relativeName);


        CompilerTestExpectAndThatInterface exceptionIsThrown(@FluentApiBackingBeanMapping(value = "exceptionIsThrown") Class<? extends Exception> exception);

        CompilerMessageCheckMessageType compilerMessage();


    }


    @FluentApiInterface(CompilerMessageCheckBB.class)
    public interface CompilerMessageCheckMessageType {

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "NOTE")
        CompilerMessageCheckComparisonType ofKindNote();

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "WARNING")
        CompilerMessageCheckComparisonType ofKindWarning();

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "MANDATORY_WARNING")
        CompilerMessageCheckComparisonType ofKindMandatoryWarning();

        @FluentApiImplicitValue(id = "compilerMessageScope", value = "ERROR")
        CompilerMessageCheckComparisonType ofKindError();

    }

    @FluentApiInterface(CompilerMessageCheckBB.class)
    public interface CompilerMessageCheckComparisonType {

        @FluentApiImplicitValue(id = "compilerMessageComparisonType", value = "CONTAINS")
        @FluentApiParentBackingBeanMapping(value = "compileMessageChecks")
        CompilerTestExpectAndThatInterface contains(
                @FluentApiBackingBeanMapping(value = "searchString", action = MappingAction.SET) String... text
        );


        @FluentApiImplicitValue(id = "compilerMessageComparisonType", value = "EQUALS")
        @FluentApiParentBackingBeanMapping(value = "compileMessageChecks")
        CompilerTestExpectAndThatInterface equals(
                @FluentApiBackingBeanMapping(value = "searchString", action = MappingAction.SET) String text
        );


        CompilerMessageCheckComparisonType atLine(@FluentApiBackingBeanMapping(value = "atLine") int line);

        CompilerMessageCheckComparisonType atColumn(@FluentApiBackingBeanMapping(value = "atColumn") int column);

        CompilerMessageCheckComparisonType atSource(@FluentApiBackingBeanMapping(value = "atSource") String column);

        CompilerMessageCheckComparisonType withLocale(@FluentApiBackingBeanMapping(value = "withLocale") Locale column);

    }

    @FluentApiInterface(GeneratedJavaFileObjectCheckBB.class)
    public interface GeneratedJavaFileObjectCheck {

        @FluentApiImplicitValue(id = "checkType", value = "EXISTS")
        @FluentApiParentBackingBeanMapping(value = "javaFileObjectChecks", action = MappingAction.ADD)
        CompilerTestExpectAndThatInterface exists();

        @FluentApiImplicitValue(id = "checkType", value = "DOESNT_EXIST")
        @FluentApiParentBackingBeanMapping(value = "javaFileObjectChecks", action = MappingAction.ADD)
        CompilerTestExpectAndThatInterface doesntExist();

        default CompilerTestExpectAndThatInterface matches(JavaFileObject expectedJavaFileObject) {
            return matches(CompileTestBuilder.ExpectedFileObjectMatcherKind.BINARY, expectedJavaFileObject);
        }

        default CompilerTestExpectAndThatInterface matches(CompileTestBuilder.ExpectedFileObjectMatcherKind expectedFileObjectMatcherKind, JavaFileObject expectedJavaFileObject) {
            return matches(expectedFileObjectMatcherKind.createMatcher(expectedJavaFileObject));
        }

        @FluentApiParentBackingBeanMapping(value = "javaFileObjectChecks", action = MappingAction.ADD)
        CompilerTestExpectAndThatInterface matches(@FluentApiBackingBeanMapping(value = "generatedFileObjectMatcher") GeneratedFileObjectMatcher generatedJavaFileObjectCheck);

    }

    @FluentApiInterface(GeneratedFileObjectCheckBB.class)
    public interface GeneratedFileObjectCheck {

        @FluentApiImplicitValue(id = "checkType", value = "EXISTS")
        @FluentApiParentBackingBeanMapping(value = "fileObjectChecks", action = MappingAction.ADD)
        CompilerTestExpectAndThatInterface exists();

        @FluentApiImplicitValue(id = "checkType", value = "DOESNT_EXIST")
        @FluentApiParentBackingBeanMapping(value = "fileObjectChecks", action = MappingAction.ADD)
        CompilerTestExpectAndThatInterface doesntExist();

        default CompilerTestExpectAndThatInterface matches(FileObject expectedFileObject) {
            return matches(CompileTestBuilder.ExpectedFileObjectMatcherKind.TEXT_IGNORE_LINE_ENDINGS, expectedFileObject);
        }

        default CompilerTestExpectAndThatInterface matches(CompileTestBuilder.ExpectedFileObjectMatcherKind expectedFileObjectMatcherKind, FileObject expectedFileObject) {
            return matches(expectedFileObjectMatcherKind.createMatcher(expectedFileObject));
        }

        @FluentApiParentBackingBeanMapping(value = "fileObjectChecks", action = MappingAction.ADD)
        CompilerTestExpectAndThatInterface matches(@FluentApiBackingBeanMapping(value = "generatedFileObjectMatcher") GeneratedFileObjectMatcher... generatedJavaFileObjectCheck);


    }


    // Commands
    @FluentApiCommand
    public static class ExecuteTestCommand {
        static void myCommand(CompilerTestBB backingBean) {
            System.out.println("DONE");
        }
    }


}