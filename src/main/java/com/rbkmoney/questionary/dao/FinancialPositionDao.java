package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;

import java.util.List;

public interface FinancialPositionDao {

    Long save(FinancialPosition financialPosition);

    void saveAll(List<FinancialPosition> financialPositionList);

    FinancialPosition getById(Long id);

    List<FinancialPosition> getByAdditionalInfoId(Long id);
}
