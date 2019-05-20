package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner;

import java.util.List;

public interface BeneficialOwnerDao {

    Long save(BeneficialOwner beneficialOwner);

    BeneficialOwner getById(Long id);

    List<BeneficialOwner> getByQuestionaryId(Long questionaryId);
}
