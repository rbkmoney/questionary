package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeneficialOwnerConverterTest {

    private BeneficialOwnerConverter beneficialOwnerConverter = new BeneficialOwnerConverter();

    private BeneficialOwner beneficialOwner;

    @Before
    public void setUp() throws Exception {
        beneficialOwner = EnhancedRandom.random(BeneficialOwner.class);
        beneficialOwner.setId(null);
        beneficialOwner.setQuestionaryId(null);
        beneficialOwner.setOwnershipPercentage((short) 50);
    }

    @Test
    public void convertTest() {
        final var beneficialOwnerThrift = beneficialOwnerConverter.convertToThrift(this.beneficialOwner);
        Assert.assertEquals(beneficialOwner, beneficialOwnerConverter.convertFromThrift(beneficialOwnerThrift));
    }
}
