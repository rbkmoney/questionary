package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.IndividualPerson;
import com.rbkmoney.questionary.InternationalLegalEntityFounder;
import com.rbkmoney.questionary.PersonAnthroponym;
import com.rbkmoney.questionary.RussianLegalEntityFounder;
import com.rbkmoney.questionary.domain.enums.FounderType;
import com.rbkmoney.questionary.domain.tables.pojos.Founder;
import com.rbkmoney.questionary.util.ThriftUtil;

public class FounderConverter implements ThriftConverter<Founder, com.rbkmoney.questionary.Founder> {
    @Override
    public com.rbkmoney.questionary.Founder convertToThrift(Founder value) {
        switch (value.getType()) {
            case legal:
                RussianLegalEntityFounder russianLegalEntityFounder = new RussianLegalEntityFounder();
                russianLegalEntityFounder.setFullName(value.getFullName());
                russianLegalEntityFounder.setInn(value.getInn());
                russianLegalEntityFounder.setOgrn(value.getOgrn());

                return com.rbkmoney.questionary.Founder.russian_legal_entity_founder(russianLegalEntityFounder);
            case individual:
                IndividualPerson individualPerson = new IndividualPerson();
                PersonAnthroponym personAnthroponym = new PersonAnthroponym();
                personAnthroponym.setFirstName(value.getFirstName());
                personAnthroponym.setSecondName(value.getSecondName());
                personAnthroponym.setMiddleName(value.getMiddleName());
                individualPerson.setInn(value.getInn());
                ThriftUtil.setIfNotEmpty(personAnthroponym, individualPerson::setFio);

                return com.rbkmoney.questionary.Founder.individual_person_founder(individualPerson);
            case international_legal:
                InternationalLegalEntityFounder internationalLegalEntityFounder = new InternationalLegalEntityFounder();
                internationalLegalEntityFounder.setCountry(value.getCountry());
                internationalLegalEntityFounder.setFullName(value.getFullName());

                return com.rbkmoney.questionary.Founder.international_legal_entity_founder(internationalLegalEntityFounder);
            default:
                throw new RuntimeException(String.format("Unknown founder type: %s", value.getType()));
        }
    }

    @Override
    public Founder convertFromThrift(com.rbkmoney.questionary.Founder value) {
        Founder founder = new Founder();
        if (value.isSetRussianLegalEntityFounder()) {
            founder.setType(FounderType.legal);
            founder.setInn(value.getRussianLegalEntityFounder().getInn());
            founder.setFullName(value.getRussianLegalEntityFounder().getFullName());
            founder.setOgrn(value.getRussianLegalEntityFounder().getOgrn());
        } else if (value.isSetIndividualPersonFounder()) {
            founder.setType(FounderType.individual);
            founder.setInn(value.getIndividualPersonFounder().getInn());
            founder.setFirstName(value.getIndividualPersonFounder().getFio().getFirstName());
            founder.setSecondName(value.getIndividualPersonFounder().getFio().getSecondName());
            founder.setMiddleName(value.getIndividualPersonFounder().getFio().getMiddleName());
        } else if (value.isSetInternationalLegalEntityFounder()) {
            founder.setType(FounderType.international_legal);
            founder.setCountry(value.getInternationalLegalEntityFounder().getCountry());
            founder.setFullName(value.getInternationalLegalEntityFounder().getFullName());
        }

        return founder;
    }
}
