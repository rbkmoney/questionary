package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.LegalOwnerInfo;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LegalOwnerConverterTest {

    private LegalOwnerConverter legalOwnerConverter = new LegalOwnerConverter();

    private LegalOwner legalOwner;

    @Before
    public void setUp() throws Exception {
        legalOwner = EnhancedRandom.random(LegalOwner.class);
        legalOwner.setId(null);
    }

    @Test
    public void convertTest() {
        final LegalOwnerInfo legalOwnerInfoThrift = legalOwnerConverter.convertToThrift(legalOwner);
        final LegalOwner legalOwner = legalOwnerConverter.convertFromThrift(legalOwnerInfoThrift);
        Assert.assertEquals(this.legalOwner, legalOwnerConverter.convertFromThrift(legalOwnerInfoThrift));
    }

}
