package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.domain.enums.BusinessInfoType;
import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BusinessInfoConverterTest {

    private BusinessInfoConverter businessInfoConverter = new BusinessInfoConverter();

    private BusinessInfo businessInfo;

    @Before
    public void setUp() throws Exception {
        this.businessInfo = new BusinessInfo();
        businessInfo.setId(null);
        businessInfo.setAdditionalInfoId(null);
        businessInfo.setType(BusinessInfoType.another_business);
        businessInfo.setDescription("Test");
    }

    @Test
    public void convertTest() {
        final var businessInfoThrift = businessInfoConverter.convertToThrift(this.businessInfo);
        Assert.assertEquals(businessInfo, businessInfoConverter.convertFromThrift(businessInfoThrift));
    }
}
