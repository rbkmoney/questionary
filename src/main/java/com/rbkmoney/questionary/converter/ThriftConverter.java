package com.rbkmoney.questionary.converter;

/**
 * Thrift conversion to jooq
 *
 * @param <T> the type of thrift
 * @param <J> the type of jooq
 */
public interface ThriftConverter<T, J> {

    T toThrift(J value, ThriftConverterContext ctx);

}
