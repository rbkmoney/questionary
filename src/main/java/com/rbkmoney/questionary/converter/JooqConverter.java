package com.rbkmoney.questionary.converter;

/**
 * Jooq conversion to thrift
 *
 * @param <J> the type of jooq
 * @param <T> the type of thrift
 */
public interface JooqConverter<J, T> {

    J toJooq(T value, JooqConverterContext ctx);

}
