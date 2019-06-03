package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.dao.*;
import com.rbkmoney.questionary.domain.tables.pojos.*;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AdditionalInfoDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    @Autowired
    private AdditionalInfoDao additionalInfoDao;

    @Autowired
    private BusinessInfoDao businessInfoDao;

    @Autowired
    private FinancialPositionDao financialPositionDao;

    private Questionary questionary;

    private AdditionalInfo additionalInfo;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        additionalInfo = EnhancedRandom.random(AdditionalInfo.class);
        additionalInfo.setId(null);
    }

    @Test
    public void saveTest() {
        Long questionaryId = questionaryDao.saveQuestionary(questionary);

        additionalInfo.setId(questionaryId);
        Long additionalInfoId = additionalInfoDao.save(additionalInfo);
        additionalInfo.setId(additionalInfoId);

        Assert.assertEquals(additionalInfo, additionalInfoDao.getById(additionalInfoId));
    }


}
