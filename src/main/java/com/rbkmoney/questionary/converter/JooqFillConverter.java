package com.rbkmoney.questionary.converter;

public interface JooqFillConverter<J, T>  {

    void fillJooq(J fillableValue, T value, JooqConverterContext ctx);

}
