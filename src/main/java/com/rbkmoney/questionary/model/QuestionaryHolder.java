package com.rbkmoney.questionary.model;

import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionaryHolder {

    private final Questionary questionary;

    private final IndividualEntityQuestionaryHolder individualEntityQuestionaryHolder;

    private final LegalEntityQuestionaryHolder legalEntityQuestionaryHolder;

}
