package com.rbkmoney.questionary.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class ConverterManager {

    private final Map<Class<?>, JooqConverter> jooqConverterMap;

    private final Map<Class<?>, JooqFillConverter> jooqFillConverterMap;

    private final Map<Class<?>, ThriftConverter> thriftConverterMap;

    private final Map<Class<?>, ThriftFillConverter> thriftFillConverterMap;

    private final JooqConverterContext jooqConverterContext;

    private final ThriftConverterContext thriftConverterContext;

    @Autowired
    public ConverterManager(List<JooqConverter> jooqConverterList,
                            List<ThriftConverter> thriftConverterList,
                            List<JooqFillConverter> jooqFillConverterList,
                            List<ThriftFillConverter> thriftFillConverterList) {
        this.jooqConverterMap = new HashMap<>();
        this.jooqFillConverterMap = new HashMap<>();
        this.thriftConverterMap = new HashMap<>();
        this.thriftFillConverterMap = new HashMap<>();

        initSwagConverterList(jooqConverterList, thriftConverterList, jooqFillConverterList, thriftFillConverterList);

        jooqConverterContext = new JooqConverterContext(
                Collections.unmodifiableMap(jooqConverterMap),
                Collections.unmodifiableMap(jooqFillConverterMap)
        );

        thriftConverterContext = new ThriftConverterContext(
                Collections.unmodifiableMap(thriftConverterMap),
                Collections.unmodifiableMap(thriftFillConverterMap)
        );
    }

    private void initSwagConverterList(List<JooqConverter> jooqConverters,
                                       List<ThriftConverter> thriftConverterList,
                                       List<JooqFillConverter> jooqFillConverterList,
                                       List<ThriftFillConverter> thriftFillConverterList) {
        for (JooqConverter jooqConverter : jooqConverters) {
            Class<?> typeArgument = getTypeArgument(jooqConverter.getClass(), JooqConverter.class);
            JooqConverter converter = jooqConverterMap.putIfAbsent(typeArgument, jooqConverter);
            if (converter != null) {
                throwError(converter.getClass(), jooqConverter.getClass());
            }
        }
        for (ThriftConverter thriftConverter : thriftConverterList) {
            Class<?> typeArgument = getTypeArgument(thriftConverter.getClass(), ThriftConverter.class);
            ThriftConverter converter = thriftConverterMap.putIfAbsent(typeArgument, thriftConverter);
            if (converter != null) {
                throwError(converter.getClass(), thriftConverter.getClass());
            }
        }
        for (JooqFillConverter jooqFillConverter : jooqFillConverterList) {
            Class<?> typeArgument = getTypeArgument(jooqFillConverter.getClass(), JooqFillConverter.class);
            JooqFillConverter converter = jooqFillConverterMap.putIfAbsent(typeArgument, jooqFillConverter);
            if (converter != null) {
                throwError(converter.getClass(), jooqFillConverter.getClass());
            }
        }
        for (ThriftFillConverter thriftFillConverter : thriftFillConverterList) {
            Class<?> typeArgument = getTypeArgument(thriftFillConverter.getClass(), ThriftFillConverter.class);
            ThriftFillConverter converter = thriftFillConverterMap.putIfAbsent(typeArgument, thriftFillConverter);
            if (converter != null) {
                throwError(converter.getClass(), thriftFillConverter.getClass());
            }
        }
    }

    private void throwError(Class<?> current, Class<?> settable) {
        String errMsg = String.format("Converter already set. Current: '%s' Trying to set: '%s'",
                current.getName(), settable.getName());
        throw new IllegalStateException(errMsg);
    }

    public <T, J> J convertFromThrift(T thriftVal, Class<J> jooqType) {
        JooqConverter jooqConverter = jooqConverterMap.get(jooqType);
        if (jooqConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + jooqType.getName());
        }

        JooqConverterContext jooqConverterContext = new JooqConverterContext(
                Collections.unmodifiableMap(jooqConverterMap),
                Collections.unmodifiableMap(jooqFillConverterMap)
        );

        return (J) jooqConverter.toJooq(thriftVal, jooqConverterContext);
    }

    public <T, J> T convertToThrift(J jooqVal, Class<T> thriftType) {
        ThriftConverter thriftConverter = thriftConverterMap.get(thriftType);
        if (thriftConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + thriftType.getName());
        }

        return (T) thriftConverter.toThrift(jooqVal, thriftConverterContext);
    }

    public <T, J> void fillJooq(J jooqVal, T thriftVal) {
        JooqFillConverter jooqFillConverter = jooqFillConverterMap.get(jooqVal.getClass());
        if (jooqFillConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + jooqVal.getClass().getName());
        }

        jooqFillConverter.fillJooq(jooqVal, thriftVal, jooqConverterContext);
    }

    public <T, J> void fillThrift(T thriftVal, J jooqVal) {
        ThriftFillConverter thriftFillConverter = thriftFillConverterMap.get(thriftVal.getClass());
        if (thriftFillConverter == null) {
            throw new IllegalArgumentException("Unregistered converter type: " + thriftVal.getClass().getName());
        }

        thriftFillConverter.fillThrift(thriftVal, jooqVal, thriftConverterContext);
    }

    private Class<?> getTypeArgument(Class<?> clazz, Class<?> converterType) {
        Type[] genericInterfaces = clazz.getGenericInterfaces();
        ParameterizedType swagParameterizedType = findConverterParamType(clazz, converterType);
        return (Class<?>) swagParameterizedType.getActualTypeArguments()[0];
    }

    private ParameterizedType findConverterParamType(Class converterImpl, Class converterType) {
        Type[] genericInterfaces = converterImpl.getGenericInterfaces();
        return Stream.of(genericInterfaces)
                .filter(type -> ((ParameterizedType) type).getRawType() == converterType)
                .map(type -> ((ParameterizedType) type))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Not found converterType interface for: %s",
                        converterImpl.getName())));
    }

}
