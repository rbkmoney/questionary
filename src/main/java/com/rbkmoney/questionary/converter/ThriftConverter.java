package com.rbkmoney.questionary.converter;

import org.apache.thrift.TBase;

/**
 * @param <O> Another object type
 * @param <T> Thrift type
 */
public interface ThriftConverter<O, T extends TBase> {

    T convertToThrift(O value);

    O convertFromThrift(T value);

}
