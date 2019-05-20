package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;

import java.util.List;

public interface BusinessInfoDao {

    Long save(BusinessInfo businessInfo);

    void saveAll(List<BusinessInfo> businessInfoList);

    BusinessInfo getById(Long id);

    List<BusinessInfo> getByAdditionalInfoId(Long id);

}
