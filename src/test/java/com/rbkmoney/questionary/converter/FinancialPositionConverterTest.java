package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.domain.enums.FinancialPosType;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FinancialPositionConverterTest {

    private FinancialPositionConverter financialPositionConverter = new FinancialPositionConverter();

    private FinancialPosition financialPosition;

    @Before
    public void setUp() throws Exception {
        financialPosition = new FinancialPosition();
        financialPosition.setType(FinancialPosType.annual_tax_return_without_mark_paper);
        financialPosition.setId(null);
        financialPosition.setAdditionalInfoId(null);
    }

    @Test
    public void convertTest() {
        final var thriftFinancialPosition = financialPositionConverter.convertToThrift(this.financialPosition);
        Assert.assertEquals(financialPosition, financialPositionConverter.convertFromThrift(thriftFinancialPosition));
    }
}
