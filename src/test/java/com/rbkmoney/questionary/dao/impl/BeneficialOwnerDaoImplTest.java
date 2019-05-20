package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.dao.BeneficialOwnerDao;
import com.rbkmoney.questionary.dao.QuestionaryDao;
import com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class BeneficialOwnerDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    @Autowired
    private BeneficialOwnerDao beneficialOwnerDao;

    private Questionary questionary;

    private BeneficialOwner beneficialOwner;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        beneficialOwner = EnhancedRandom.random(BeneficialOwner.class);
    }

    @Test
    public void saveTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        beneficialOwner.setQuestionaryId(questionaryId);
        final Long beneficialOwnerId = beneficialOwnerDao.save(beneficialOwner);
        beneficialOwner.setId(beneficialOwnerId);
        Assert.assertEquals(beneficialOwner, beneficialOwnerDao.getById(beneficialOwnerId));
    }
}
