package io.toolisticon.fluapigen;

public class TestUsage {

    public static void main (String[] args) {
        CuteFluentApi.CompilerTestInterface tst = CuteFluentApiStarter.unitTest()
                .compilationShouldSucceed()
                .expectCompilerMessage().asError().atLine(10).thatContains("WTF")
                .expectCompilerMessage().asError().atLine(11).thatContains("WTF2")
                ;
        System.out.println("");
    }

}
