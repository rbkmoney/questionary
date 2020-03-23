package com.rbkmoney.questionary.handler;

import com.rbkmoney.questionary.exception.QuestionaryNotFoundException;
import com.rbkmoney.questionary.exception.QuestionaryNotValidException;
import com.rbkmoney.questionary.exception.QuestionaryVersionConflictException;
import com.rbkmoney.questionary.manage.*;
import com.rbkmoney.questionary.service.QuestionaryService;
import com.rbkmoney.woody.api.flow.error.WUndefinedResultException;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class QuestionaryHandler implements QuestionaryManagerSrv.Iface {

    private QuestionaryService questionaryService;

    public QuestionaryHandler(QuestionaryService questionaryService) {
        this.questionaryService = questionaryService;
    }

    @Override
    public long save(QuestionaryParams questionaryParams, long version) throws QuestionaryNotValid, QuestionaryVersionConflict, TException {
        try {
            return questionaryService.saveQuestionary(questionaryParams, version);
        } catch (QuestionaryNotValidException ex) {
            log.warn("Questionary not valid, ownerId={}", questionaryParams.getOwnerId(), ex);
            throw new QuestionaryNotValid();
        } catch (QuestionaryVersionConflictException ex) {
            log.warn("Questionary version conflict, version={}", version, ex);
            throw new QuestionaryVersionConflict();
        } catch (Exception ex) {
            throw undefinedResultException("save", ex);
        }
    }

    @Override
    public Snapshot get(String questionaryId, String partyId, Reference reference) throws QuestionaryNotFound, TException {
        try {
            return questionaryService.getQuestionary(questionaryId, partyId, reference);
        } catch (QuestionaryNotFoundException ex) {
            log.warn("Questionary not found, claimId={}", questionaryId, ex);
            throw new QuestionaryNotFound();
        } catch (Exception ex) {
            throw undefinedResultException("get", ex);
        }
    }

    private WUndefinedResultException undefinedResultException(String msg, Exception ex) {
        log.error("Error during '{}' process", msg, ex);
        return new WUndefinedResultException(msg, ex);
    }
}
