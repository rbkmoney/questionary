package com.rbkmoney.questionary.model;

import com.rbkmoney.questionary.domain.tables.pojos.InternationalBankInfo;
import com.rbkmoney.questionary.domain.tables.pojos.InternationalLegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class InternationalLegalEntityQuestionaryHolder {

    private Questionary questionary;

    private InternationalLegalEntityQuestionary internationalLegalEntityQuestionary;

    private InternationalBankInfo internationalBankInfo;

}
