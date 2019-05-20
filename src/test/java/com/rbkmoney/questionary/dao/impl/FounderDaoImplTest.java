package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.dao.FounderDao;
import com.rbkmoney.questionary.dao.QuestionaryDao;
import com.rbkmoney.questionary.domain.tables.pojos.Founder;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class FounderDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    @Autowired
    private FounderDao founderDao;

    private Questionary questionary;

    private Founder founder;

    private List<Founder> founderList;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        founder = EnhancedRandom.random(Founder.class);
        founder.setId(null);
        founder.setQuestionaryId(null);
        founderList = EnhancedRandom.randomListOf(5, Founder.class, "id");
    }

    @Test
    public void saveTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        founder.setQuestionaryId(questionaryId);
        final Long founderId = founderDao.save(founder);
        founder.setId(founderId);
        Assert.assertEquals(founder, founderDao.getById(founderId));
    }

    @Test
    public void saveAllTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        founderList = founderList.stream()
                .peek(founder -> founder.setQuestionaryId(questionaryId))
                .collect(Collectors.toList());
        founderDao.saveAll(founderList);
        Assert.assertEquals(5, founderDao.getByQuestionaryId(questionaryId).size());
    }
}
