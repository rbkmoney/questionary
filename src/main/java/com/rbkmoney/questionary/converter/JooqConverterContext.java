package com.rbkmoney.questionary.converter;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JooqConverterContext {

    private final Map<Class<?>, JooqConverter> converterMap;

    private final Map<Class<?>, JooqFillConverter> fillConverterMap;

    public <T, J> J convert(T thriftVal, Class<J> jooqType) {
        JooqConverter jooqConverter = converterMap.get(jooqType);
        if (jooqConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + jooqType.getName());
        }

        return (J) jooqConverter.toJooq(thriftVal, this);
    }

    public <T, J> void fill(J jooqVal, T thriftVal) {
        JooqFillConverter jooqFillConverter = fillConverterMap.get(jooqVal.getClass());
        if (jooqFillConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + jooqVal.getClass().getName());
        }

        jooqFillConverter.fillJooq(jooqVal, thriftVal, this);
    }

}
