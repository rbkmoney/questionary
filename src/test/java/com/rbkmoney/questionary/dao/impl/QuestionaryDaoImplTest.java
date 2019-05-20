package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.IndividualEntity;
import com.rbkmoney.questionary.LegalEntity;
import com.rbkmoney.questionary.dao.*;
import com.rbkmoney.questionary.domain.tables.pojos.*;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QuestionaryDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    private Questionary questionary;
    private IndividualEntityQuestionary individualEntityQuestionary;
    private LegalEntityQuestionary legalEntityQuestionary;

    @Before
    public void before() {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        questionary.setVersion(1L);
        individualEntityQuestionary = EnhancedRandom.random(IndividualEntityQuestionary.class);
        individualEntityQuestionary.setId(null);
        legalEntityQuestionary = EnhancedRandom.random(LegalEntityQuestionary.class);
        legalEntityQuestionary.setId(null);
    }

    @Test
    public void saveQuestionaryTest() {
        questionary.setVersion(1L);
        Long id = questionaryDao.saveQuestionary(questionary);
        questionary.setId(id);

        assertEquals(questionary, questionaryDao.getQuestionaryById(id));
    }

    @Test
    public void getLatestQuestionaryTest() {
        Long expectedVersion = 2L;
        questionaryDao.saveQuestionary(questionary);
        questionary.setVersion(expectedVersion);
        questionaryDao.saveQuestionary(questionary);
        Questionary questionary = questionaryDao.getLatestQuestionary(this.questionary.getQuestionaryId());

        assertEquals(expectedVersion, questionary.getVersion());
    }

    @Test
    public void getQuestionaryByIdAndVersionTest() {
        Long id = questionaryDao.saveQuestionary(questionary);
        questionary.setId(id);
        assertEquals(questionary, questionaryDao.getQuestionaryByIdAndVersion(questionary.getQuestionaryId(), questionary.getVersion()));
    }

    @Test
    public void saveIndividualEntityQuestionaryTest() {
        Long questionaryId = questionaryDao.saveQuestionary(questionary);
        individualEntityQuestionary.setId(questionaryId);

        Long individualEntityId = questionaryDao.saveIndividualEntity(individualEntityQuestionary);

        IndividualEntity individualEntity = questionaryDao.getIndividualEntityById(individualEntityId);

        assertNotNull(individualEntity);
    }

    @Test
    public void saveLegalEntityQuestionaryTest() {
        Long questionaryId = questionaryDao.saveQuestionary(questionary);
        legalEntityQuestionary.setId(questionaryId);
        legalEntityQuestionary.setLegalOwnerId(null);

        Long legalEntityId = questionaryDao.saveLegalEntity(legalEntityQuestionary);

        LegalEntity legalEntity = questionaryDao.getLegalEntityById(legalEntityId);

        assertNotNull(legalEntity);
    }

}
