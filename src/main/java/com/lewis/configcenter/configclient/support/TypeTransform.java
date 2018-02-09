package com.lewis.configcenter.configclient.support;


import com.google.common.base.Function;

/**
 * @author zhangminghua
 */
public interface TypeTransform {

    public Function<String, Integer> integerFunction = new Function<String, Integer>() {
        @Override
        public Integer apply(String input) {
            return Integer.parseInt(input);
        }
    };

    public Function<String, Long> longFunction = new Function<String, Long>() {
        @Override
        public Long apply(String input) {
            return Long.parseLong(input);
        }
    };

    public Function<String, Short> shortFunction = new Function<String, Short>() {
        @Override
        public Short apply(String input) {
            return Short.parseShort(input);
        }
    };

    public Function<String, Byte> byteFunction = new Function<String, Byte>() {
        @Override
        public Byte apply(String input) {
            return Byte.parseByte(input);
        }
    };

    public Function<String, Boolean> booleanFunction = new Function<String, Boolean>() {
        @Override
        public Boolean apply(String input) {
            return Boolean.parseBoolean(input);
        }
    };

    public Function<String, Double> doubleFunction = new Function<String, Double>() {
        @Override
        public Double apply(String input) {
            return Double.parseDouble(input);
        }
    };

    public Function<String, Float> flatFunction = new Function<String, Float>() {
        @Override
        public Float apply(String input) {
            return Float.parseFloat(input);
        }
    };

}
