package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;

public interface LegalOwnerDao {

    Long save(LegalOwner legalOwner);

    LegalOwner getById(Long id);

}
