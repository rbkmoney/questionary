package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.dao.AdditionalInfoDao;
import com.rbkmoney.questionary.dao.BusinessInfoDao;
import com.rbkmoney.questionary.dao.QuestionaryDao;
import com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo;
import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessInfoDaoImplTest {

    @Autowired
    private QuestionaryDao questionaryDao;

    @Autowired
    private BusinessInfoDao businessInfoDao;

    @Autowired
    private AdditionalInfoDao additionalInfoDao;

    private Questionary questionary;

    private AdditionalInfo additionalInfo;

    private BusinessInfo businessInfo;

    private List<BusinessInfo> businessInfoList;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        additionalInfo = EnhancedRandom.random(AdditionalInfo.class);
        additionalInfo.setId(null);
        businessInfo = EnhancedRandom.random(BusinessInfo.class);
        businessInfo.setId(null);
        businessInfo.setAdditionalInfoId(null);
        businessInfoList = EnhancedRandom.randomListOf(5, BusinessInfo.class, "id");
    }

    @Test
    public void saveTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        additionalInfo.setId(questionaryId);
        final Long additonalInfoId = additionalInfoDao.save(additionalInfo);
        businessInfo.setAdditionalInfoId(additonalInfoId);
        final Long businessInfoId = businessInfoDao.save(businessInfo);
        businessInfo.setId(businessInfoId);
        Assert.assertEquals(businessInfo, businessInfoDao.getById(businessInfoId));
    }

    @Test
    public void saveAllTest() {
        final Long questionaryId = questionaryDao.saveQuestionary(questionary);
        additionalInfo.setId(questionaryId);
        final Long additionalInfoId = additionalInfoDao.save(additionalInfo);
        businessInfoList = businessInfoList.stream()
                .peek(businessInfo -> businessInfo.setAdditionalInfoId(additionalInfoId))
                .collect(Collectors.toList());
        businessInfoDao.saveAll(businessInfoList);
        Assert.assertEquals(5, businessInfoDao.getByAdditionalInfoId(additionalInfoId));
    }
}
