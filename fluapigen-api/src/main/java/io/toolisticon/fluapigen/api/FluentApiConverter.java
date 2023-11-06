package io.toolisticon.fluapigen.api;

/**
 * Simple converter interface for fluent api parameters,
 *
 * @param <SOURCE> The source type
 * @param <TARGET> The target type
 */
public interface FluentApiConverter<SOURCE, TARGET> {
    TARGET convert(SOURCE source);


    class NoConversion<TYPE> implements FluentApiConverter<TYPE, TYPE> {
        @Override
        public TYPE convert(TYPE type) {
            return type;
        }
    }

}
