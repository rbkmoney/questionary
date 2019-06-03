package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo;
import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import com.rbkmoney.questionary.model.AdditionalInfoHolder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AdditionalInfoConverterTest {

    private AdditionalInfoConverter additionalInfoConverter = new AdditionalInfoConverter();

    private AdditionalInfoHolder additionalInfoHolder;

    @Before
    public void setUp() throws Exception {
        final AdditionalInfo additionalInfo = EnhancedRandom.random(AdditionalInfo.class);
        additionalInfo.setId(null);
        final var additionalInfoHolderBuilder = AdditionalInfoHolder.builder();
        additionalInfoHolderBuilder.additionalInfo(additionalInfo);
        additionalInfoHolder = additionalInfoHolderBuilder.build();
    }

    @Test
    public void convertTest() {
        final var additionalInfoThrift = additionalInfoConverter.convertToThrift(additionalInfoHolder);
        final AdditionalInfoHolder additionalInfoHolder = additionalInfoConverter.convertFromThrift(additionalInfoThrift);
        Assert.assertEquals(this.additionalInfoHolder.getAdditionalInfo(), additionalInfoHolder.getAdditionalInfo());
    }
}
