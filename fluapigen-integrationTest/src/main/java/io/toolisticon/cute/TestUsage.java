package io.toolisticon.cute;


import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class TestUsage {

    public static void main(String[] args) {
        CuteFluentApi.CompilerTestExpectAndThatInterface tst = CuteFluentApiStarter.unitTest()
                .expectThat().exceptionIsThrown(IllegalArgumentException.class)
                .andThat().javaFileObject(StandardLocation.CLASS_OUTPUT, "Wtf1", JavaFileObject.Kind.CLASS).exists()
                .andThat().javaFileObject(StandardLocation.CLASS_OUTPUT, "Wtf2", JavaFileObject.Kind.CLASS).exists()
                .andThat().generatedClass("Wtf3").exists()
                .andThat().compilationSucceeds()
                .andThat().compilerMessage().ofKindError().atLine(10).atColumn(5).contains("WTF")
                .andThat().compilerMessage().ofKindError().atLine(11).contains("WTF2");


        tst.executeTest();
        System.out.println("");

        CompileTestBuilder.unitTest();

    }

}
