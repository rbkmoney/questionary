package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.questionary.IndividualPerson;
import com.rbkmoney.questionary.InternationalLegalEntityFounder;
import com.rbkmoney.questionary.RussianLegalEntityFounder;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.enums.FounderType;
import com.rbkmoney.questionary.domain.tables.pojos.Founder;
import org.springframework.stereotype.Component;

@Component
public class FounderConverter implements ThriftConverter<com.rbkmoney.questionary.Founder, Founder>,
        JooqConverter<Founder, com.rbkmoney.questionary.Founder> {

    @Override
    public com.rbkmoney.questionary.Founder toThrift(Founder value, ThriftConverterContext ctx) {
        switch (value.getType()) {
            case legal:
                RussianLegalEntityFounder russianLegalEntityFounder = new RussianLegalEntityFounder();
                russianLegalEntityFounder.setFullName(value.getFullName());
                russianLegalEntityFounder.setInn(value.getInn());
                russianLegalEntityFounder.setOgrn(value.getOgrn());

                return com.rbkmoney.questionary.Founder.russian_legal_entity_founder(russianLegalEntityFounder);
            case individual:
                IndividualPerson individualPerson = new IndividualPerson();
                individualPerson.setFio(value.getFio());
                individualPerson.setInn(value.getInn());

                return com.rbkmoney.questionary.Founder.individual_person_founder(individualPerson);
            case international_legal:
                InternationalLegalEntityFounder internationalLegalEntityFounder = new InternationalLegalEntityFounder();
                internationalLegalEntityFounder.setCountry(value.getCountry());
                internationalLegalEntityFounder.setFullName(value.getFullName());

                return com.rbkmoney.questionary.Founder.international_legal_entity_founder(internationalLegalEntityFounder);
            default:
                throw new IllegalArgumentException(String.format("Unknown founder type: %s", value.getType()));
        }
    }

    @Override
    public Founder toJooq(com.rbkmoney.questionary.Founder value, JooqConverterContext ctx) {
        Founder founder = new Founder();
        if (value.isSetRussianLegalEntityFounder()) {
            founder.setType(FounderType.legal);
            founder.setInn(value.getRussianLegalEntityFounder().getInn());
            founder.setFullName(value.getRussianLegalEntityFounder().getFullName());
            founder.setOgrn(value.getRussianLegalEntityFounder().getOgrn());
        } else if (value.isSetIndividualPersonFounder()) {
            founder.setType(FounderType.individual);
            founder.setInn(value.getIndividualPersonFounder().getInn());
            founder.setFio(value.getIndividualPersonFounder().getFio());
        } else if (value.isSetInternationalLegalEntityFounder()) {
            founder.setType(FounderType.international_legal);
            founder.setCountry(value.getInternationalLegalEntityFounder().getCountry());
            founder.setFullName(value.getInternationalLegalEntityFounder().getFullName());
        }

        return founder;
    }
}
