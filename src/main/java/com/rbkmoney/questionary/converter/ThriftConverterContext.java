package com.rbkmoney.questionary.converter;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class ThriftConverterContext {

    private final Map<Class<?>, ThriftConverter> converterMap;

    private final Map<Class<?>, ThriftFillConverter> fillConverterMap;

    public <T, J> T convert(J jooqVal, Class<T> thriftType) {
        ThriftConverter thriftConverter = converterMap.get(thriftType);
        if (thriftConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + thriftType.getName());
        }

        return (T) thriftConverter.toThrift(jooqVal, this);
    }

    public <T, J> void fill(T thriftVal, J jooqVal) {
        ThriftFillConverter thriftFillConverter = fillConverterMap.get(thriftVal.getClass());
        if (thriftFillConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + thriftVal.getClass().getName());
        }

        thriftFillConverter.fillThrift(jooqVal, thriftVal, this);
    }

}
