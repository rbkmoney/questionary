package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.dao.HeadDao;
import com.rbkmoney.questionary.dao.QuestionaryDao;
import com.rbkmoney.questionary.domain.tables.pojos.Head;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class HeadDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    @Autowired
    private HeadDao headDao;

    private Questionary questionary;

    private Head head;

    private List<Head> headList;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        head = EnhancedRandom.random(Head.class);
        head.setId(null);
        head.setQuestionaryId(null);
        headList = EnhancedRandom.randomListOf(5, Head.class, "id");
    }

    @Test
    public void saveTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        head.setQuestionaryId(questionaryId);
        final Long headId = headDao.save(head);
        head.setId(headId);
        Assert.assertEquals(head, headDao.getById(headId));
    }

    @Test
    public void saveAllTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        headList = headList.stream()
                .peek(founder -> founder.setQuestionaryId(questionaryId))
                .collect(Collectors.toList());
        headDao.saveAll(headList);
        Assert.assertEquals(5, headDao.getByQuestionaryId(questionaryId).size());
    }

}
