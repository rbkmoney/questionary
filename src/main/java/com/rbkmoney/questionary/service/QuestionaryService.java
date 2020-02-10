package com.rbkmoney.questionary.service;

import com.rbkmoney.questionary.manage.*;

public interface QuestionaryService {

    long saveQuestionary(QuestionaryParams questionaryParams, Long version) throws QuestionaryVersionConflict;

    Snapshot getQuestionary(String questionaryId, Reference reference) throws QuestionaryNotFound;

}
