package io.toolisticon.fluapigen.processor;

public abstract class CompilerErrorException extends RuntimeException{
    public abstract void writeErrorCompilerMessage();
}
