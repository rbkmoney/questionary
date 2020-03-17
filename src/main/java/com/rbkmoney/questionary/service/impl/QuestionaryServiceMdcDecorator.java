package com.rbkmoney.questionary.service.impl;

import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.service.QuestionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@RequiredArgsConstructor
public class QuestionaryServiceMdcDecorator implements QuestionaryService {

    private static final String QUESTIONARY_ID = "questionary_id";

    private static final String PARTY_ID = "party_id";

    private final QuestionaryService questionaryService;

    @Override
    public long saveQuestionary(QuestionaryParams questionaryParams, Long version) {
        try {
            MDC.put(QUESTIONARY_ID, questionaryParams.getId());
            MDC.put(PARTY_ID, questionaryParams.getPartyId());
            return questionaryService.saveQuestionary(questionaryParams, version);
        } finally {
            MDC.clear();
        }
    }

    @Override
    public Snapshot getQuestionary(String questionaryId, String partyId, Reference reference) {
        try {
            MDC.put(QUESTIONARY_ID, questionaryId);
            MDC.put(PARTY_ID, partyId);
            return questionaryService.getQuestionary(questionaryId, partyId, reference);
        } finally {
            MDC.clear();
        }
    }
}
