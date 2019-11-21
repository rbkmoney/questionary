package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.questionary.IndividualPerson;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.Head;
import com.rbkmoney.questionary.util.ThriftUtil;
import org.springframework.stereotype.Component;

@Component
public class HeadConverter implements ThriftConverter<com.rbkmoney.questionary.Head, Head>, JooqConverter<Head, com.rbkmoney.questionary.Head> {

    @Override
    public com.rbkmoney.questionary.Head toThrift(Head value, ThriftConverterContext ctx) {
        com.rbkmoney.questionary.Head head = new com.rbkmoney.questionary.Head();
        head.setPosition(value.getPosition());
        IndividualPerson individualPerson = new IndividualPerson();
        individualPerson.setInn(value.getInn());
        individualPerson.setFio(value.getFio());
        ThriftUtil.setIfNotEmpty(individualPerson, head::setIndividualPerson);

        return head;
    }

    @Override
    public Head toJooq(com.rbkmoney.questionary.Head value, JooqConverterContext ctx) {
        Head head = new Head();
        if (value.isSetIndividualPerson()) {
            head.setInn(value.getIndividualPerson().getInn());
            if (value.getIndividualPerson().getFio() != null) {
                head.setFio(value.getIndividualPerson().getFio());
            }
        }
        head.setPosition(value.getPosition());

        return head;
    }
}
