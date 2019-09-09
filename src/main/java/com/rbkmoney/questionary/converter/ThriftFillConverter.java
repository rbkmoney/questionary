package com.rbkmoney.questionary.converter;

public interface ThriftFillConverter<T, J> {

    void fillThrift(T fillableValue, J value, ThriftConverterContext ctx);

}
