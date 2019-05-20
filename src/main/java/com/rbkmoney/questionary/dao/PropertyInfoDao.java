package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.PropertyInfo;

import java.util.List;

public interface PropertyInfoDao {

    Long save(PropertyInfo propertyInfo);

    void saveAll(List<PropertyInfo> propertyInfoList);

    PropertyInfo getById(Long id);

    List<PropertyInfo> getByQuestionaryId(Long questionaryId);

}
