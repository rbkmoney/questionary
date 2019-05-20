package com.rbkmoney.questionary.handler;

import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.service.QuestionaryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

@Slf4j
public class QuestionaryHandler implements QuestionaryManagerSrv.Iface {

    private QuestionaryService questionaryService;

    public QuestionaryHandler(QuestionaryService questionaryService) {
        this.questionaryService = questionaryService;
    }

    @Override
    public long save(QuestionaryParams questionaryParams, long version)
            throws QuestionaryNotFound, QuestionaryNotValidException, QuestionaryVersionConflict, TException {
        log.info("Save questionary: id={}, version={}", questionaryParams.getId(), version);
        final long latestVer = questionaryService.saveQuestionary(questionaryParams, version);
        log.info("Questionary successfully saved: id={}", questionaryParams.getId());
        return latestVer;
    }

    @Override
    public Snapshot get(String questionaryId, Reference reference) throws QuestionaryNotFound, TException {
        return questionaryService.getQuestionary(questionaryId, reference);
    }
}
