package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.dao.AdditionalInfoDao;
import com.rbkmoney.questionary.dao.FinancialPositionDao;
import com.rbkmoney.questionary.dao.QuestionaryDao;
import com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class FinancialPositionDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    @Autowired
    private AdditionalInfoDao additionalInfoDao;

    @Autowired
    private FinancialPositionDao financialPositionDao;

    private Questionary questionary;

    private AdditionalInfo additionalInfo;

    private List<FinancialPosition> financialPositionList;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        additionalInfo = EnhancedRandom.random(AdditionalInfo.class);
        additionalInfo.setId(null);
        financialPositionList = EnhancedRandom.randomListOf(5, FinancialPosition.class, "id");
    }

    @Test
    public void saveAllTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        additionalInfo.setId(questionaryId);
        final Long additionalInfoId = additionalInfoDao.save(additionalInfo);
        financialPositionList = financialPositionList.stream()
                .peek(financialPosition -> financialPosition.setAdditionalInfoId(additionalInfoId))
                .collect(Collectors.toList());
        financialPositionDao.saveAll(financialPositionList);
        Assert.assertEquals(5, financialPositionDao.getByAdditionalInfoId(additionalInfoId).size());
    }
}
