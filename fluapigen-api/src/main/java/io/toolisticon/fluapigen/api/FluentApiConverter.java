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

    class LongToIntegerConverter implements FluentApiConverter<Long, Integer> {
        @Override
        public Integer convert(Long value) {
            return value == null ? null : value.intValue();
        }
    }

    class IntegerToLongConverter implements FluentApiConverter<Integer, Long> {
        @Override
        public Long convert(Integer value) {
            return value == null ? null : value.longValue();
        }
    }

    class IntegerToFloatConverter implements FluentApiConverter<Integer, Float> {
        @Override
        public Float convert(Integer value) {
            return value == null ? null : value.floatValue();
        }
    }

    class IntegerToDoubleConverter implements FluentApiConverter<Integer, Double> {
        @Override
        public Double convert(Integer value) {
            return value == null ? null : value.doubleValue();
        }
    }

    class StringToDoubleConverter implements FluentApiConverter<String, Integer> {
        @Override
        public Integer convert(String value) {
            try {
                return value == null ? null : Integer.valueOf(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Passed String '"+ value + "' can't be converted into an Integer");
            }
        }
    }


}
