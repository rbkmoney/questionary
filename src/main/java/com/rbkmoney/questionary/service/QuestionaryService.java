package com.rbkmoney.questionary.service;

import com.rbkmoney.questionary.AdditionalInfo;
import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import com.rbkmoney.questionary.model.LegalEntityQuestionaryHolder;

public interface QuestionaryService {

    long saveQuestionary(QuestionaryParams questionaryParams, Long version) throws QuestionaryVersionConflict;

    Snapshot getQuestionary(String questionaryId, Reference reference) throws QuestionaryNotFound;

}
