package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.questionary.AbstractIntegrationTest;
import com.rbkmoney.questionary.dao.LegalOwnerDao;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LegalOwnerDaoImplTest extends AbstractIntegrationTest {

    @Autowired
    private LegalOwnerDao legalOwnerDao;

    private LegalOwner legalOwner;

    @Before
    public void setUp() throws Exception {
        legalOwner = EnhancedRandom.random(LegalOwner.class);
    }

    @Test
    public void saveTest() {
        Long legalOwnerId = legalOwnerDao.save(legalOwner);
        Assert.assertEquals(legalOwner, legalOwnerDao.getById(legalOwnerId));
    }
}
