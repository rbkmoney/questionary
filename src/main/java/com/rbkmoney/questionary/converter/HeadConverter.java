package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.IndividualPerson;
import com.rbkmoney.questionary.PersonAnthroponym;
import com.rbkmoney.questionary.domain.tables.pojos.Head;
import com.rbkmoney.questionary.util.ThriftUtil;

public class HeadConverter implements ThriftConverter<Head, com.rbkmoney.questionary.Head> {
    @Override
    public com.rbkmoney.questionary.Head convertToThrift(Head value) {
        com.rbkmoney.questionary.Head head = new com.rbkmoney.questionary.Head();
        head.setPosition(value.getPosition());
        IndividualPerson individualPerson = new IndividualPerson();
        individualPerson.setInn(value.getInn());
        PersonAnthroponym personAnthroponym = new PersonAnthroponym();
        personAnthroponym.setFirstName(value.getFirstName());
        personAnthroponym.setSecondName(value.getSecondName());
        personAnthroponym.setMiddleName(value.getMiddleName());
        ThriftUtil.setIfNotEmpty(personAnthroponym, individualPerson::setFio);
        ThriftUtil.setIfNotEmpty(individualPerson, head::setIndividualPerson);

        return head;
    }

    @Override
    public Head convertFromThrift(com.rbkmoney.questionary.Head value) {
        Head head = new Head();
        if (value.isSetIndividualPerson()) {
            head.setInn(value.getIndividualPerson().getInn());
            if (value.getIndividualPerson().getFio() != null) {
                head.setFirstName(value.getIndividualPerson().getFio().getFirstName());
                head.setSecondName(value.getIndividualPerson().getFio().getSecondName());
                head.setMiddleName(value.getIndividualPerson().getFio().getMiddleName());
            }
        }
        head.setPosition(value.getPosition());

        return head;
    }
}
