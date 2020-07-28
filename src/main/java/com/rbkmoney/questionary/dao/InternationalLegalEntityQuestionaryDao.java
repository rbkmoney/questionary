package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.InternationalLegalEntityQuestionary;

public interface InternationalLegalEntityQuestionaryDao {

    Long save(InternationalLegalEntityQuestionary internationalBankInfo);

    InternationalLegalEntityQuestionary getById(Long id);

    InternationalLegalEntityQuestionary getByExtId(Long extId);

}
