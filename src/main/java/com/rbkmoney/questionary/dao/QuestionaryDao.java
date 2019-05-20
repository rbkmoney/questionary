package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.IndividualEntity;
import com.rbkmoney.questionary.LegalEntity;
import com.rbkmoney.questionary.domain.tables.pojos.IndividualEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.LegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;

public interface QuestionaryDao {

    Long saveQuestionary(Questionary questionary);

    Questionary getQuestionaryById(Long id);

    Questionary getLatestQuestionary(String questionaryId);

    Questionary getQuestionaryByIdAndVersion(String questionaryId, Long version);

    Long saveIndividualEntity(IndividualEntityQuestionary questionary);

    IndividualEntity getIndividualEntityById(Long id);

    Long saveLegalEntity(LegalEntityQuestionary questionary);

    LegalEntity getLegalEntityById(Long id);
}
