package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.InternationalBankInfo;

public interface InternationalBankInfoDao {

    Long save(InternationalBankInfo internationalBankInfo);

    InternationalBankInfo getById(Long id);

    InternationalBankInfo getByExtId(Long extId);

}
