package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.dao.PropertyInfoDao;
import com.rbkmoney.questionary.dao.QuestionaryDao;
import com.rbkmoney.questionary.domain.tables.pojos.PropertyInfo;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyInfoDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    @Autowired
    private PropertyInfoDao propertyInfoDao;

    private Questionary questionary;

    private PropertyInfo propertyInfo;

    private List<PropertyInfo> propertyInfoList;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        propertyInfo = EnhancedRandom.random(PropertyInfo.class);
        propertyInfo.setId(null);
        propertyInfo.setQuestionaryId(null);
        propertyInfoList = EnhancedRandom.randomListOf(5, PropertyInfo.class, "id");
    }

    @Test
    public void saveTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        propertyInfo.setQuestionaryId(questionaryId);
        final Long propertyInfoId = propertyInfoDao.save(propertyInfo);
        propertyInfo.setId(propertyInfoId);
        Assert.assertEquals(propertyInfo, propertyInfoDao.getById(propertyInfoId));
    }

    @Test
    public void saveAllTest() {
        Long questionaryId = questionaryDao.saveQuestionary(questionary);
        propertyInfoList = propertyInfoList.stream()
                .peek(founder -> founder.setQuestionaryId(questionaryId))
                .collect(Collectors.toList());
        propertyInfoDao.saveAll(propertyInfoList);
        Assert.assertEquals(5, propertyInfoDao.getByQuestionaryId(questionaryId).size());
    }

}
