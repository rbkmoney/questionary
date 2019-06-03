package com.rbkmoney.questionary.dao;

import com.rbkmoney.questionary.domain.tables.pojos.Founder;

import java.util.List;

public interface FounderDao {

    Long save(Founder founder);

    void saveAll(List<Founder> founderList);

    Founder getById(Long id);

    List<Founder> getByQuestionaryId(Long id);

}
