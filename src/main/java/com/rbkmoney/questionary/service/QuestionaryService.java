package com.rbkmoney.questionary.service;

import com.rbkmoney.questionary.manage.QuestionaryParams;
import com.rbkmoney.questionary.manage.Reference;
import com.rbkmoney.questionary.manage.Snapshot;

public interface QuestionaryService {

    long saveQuestionary(QuestionaryParams questionaryParams, Long version);

    Snapshot getQuestionary(String questionaryId, Reference reference);

}
