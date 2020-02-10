package com.rbkmoney.questionary.service;

import com.rbkmoney.questionary.manage.*;

public interface QuestionaryService {

    long saveQuestionary(QuestionaryParams questionaryParams, Long version);

    Snapshot getQuestionary(String questionaryId, Reference reference);

}
