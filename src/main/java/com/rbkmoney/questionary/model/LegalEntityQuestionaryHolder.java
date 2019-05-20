package com.rbkmoney.questionary.model;

import com.rbkmoney.questionary.domain.tables.pojos.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class LegalEntityQuestionaryHolder {

    private Questionary questionary;

    private LegalEntityQuestionary legalEntityQuestionary;

    private LegalOwner legalOwner;

    private List<Founder> founderList;

    private List<Head> headList;

    private Head head;

    private List<PropertyInfo> propertyInfoList;

    private AdditionalInfoHolder additionalInfoHolder;

    private List<BeneficialOwner> beneficialOwnerList;

}
