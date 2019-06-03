package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo;

public interface AdditionalInfoDao {

    Long save(AdditionalInfo additionalInfo);

    AdditionalInfo getById(Long id);

}
