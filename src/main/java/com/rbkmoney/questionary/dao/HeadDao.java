package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.Head;

import java.util.List;

public interface HeadDao {

    Long save(Head head);

    void saveAll(List<Head> headList);

    Head getById(Long id);

    List<Head> getByQuestionaryId(Long id);

}
