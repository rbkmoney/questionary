package com.rbkmoney.questionary.converter.questionary;

import com.rbkmoney.questionary.InternationalLegalEntity;
import com.rbkmoney.questionary.converter.JooqConverter;
import com.rbkmoney.questionary.converter.JooqConverterContext;
import com.rbkmoney.questionary.converter.ThriftConverter;
import com.rbkmoney.questionary.converter.ThriftConverterContext;
import com.rbkmoney.questionary.domain.tables.pojos.InternationalLegalEntityQuestionary;
import org.springframework.stereotype.Component;

@Component
public class InternationalLegalEntityQuestionaryConverter
        implements ThriftConverter<InternationalLegalEntity, InternationalLegalEntityQuestionary>,
        JooqConverter<InternationalLegalEntityQuestionary, InternationalLegalEntity> {

    @Override
    public InternationalLegalEntity toThrift(InternationalLegalEntityQuestionary value,
                                             ThriftConverterContext ctx) {
        InternationalLegalEntity internationalLegalEntity = new InternationalLegalEntity();
        internationalLegalEntity.setLegalName(value.getLegalName());
        internationalLegalEntity.setTradingName(value.getTradingName());
        internationalLegalEntity.setRegisteredNumber(value.getRegisteredNumber());
        internationalLegalEntity.setActualAddress(value.getActualAddress());
        internationalLegalEntity.setRegisteredAddress(value.getRegisteredAddress());
        return internationalLegalEntity;
    }

    @Override
    public InternationalLegalEntityQuestionary toJooq(InternationalLegalEntity internationalLegalEntity,
                                                            JooqConverterContext ctx) {
        InternationalLegalEntityQuestionary questionary = new InternationalLegalEntityQuestionary();
        questionary.setLegalName(internationalLegalEntity.getLegalName());
        questionary.setTradingName(internationalLegalEntity.getTradingName());
        questionary.setRegisteredNumber(internationalLegalEntity.getRegisteredNumber());
        questionary.setActualAddress(internationalLegalEntity.getActualAddress());
        questionary.setRegisteredAddress(internationalLegalEntity.getRegisteredAddress());
        return questionary;
    }
}
