package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.domain.enums.FounderType;
import com.rbkmoney.questionary.domain.tables.pojos.Founder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FounderConverterTest {

    private FounderConverter founderConverter = new FounderConverter();

    private Founder founder;

    @Before
    public void setUp() throws Exception {
        founder = new Founder();
        founder.setType(FounderType.international_legal);
        founder.setCountry("Country");
        founder.setFullName("FullName");
        founder.setId(null);
        founder.setQuestionaryId(null);
    }

    @Test
    public void convertTest() {
        final com.rbkmoney.questionary.Founder thriftFounder = founderConverter.convertToThrift(this.founder);
        final Founder founder = founderConverter.convertFromThrift(thriftFounder);
        Assert.assertEquals(this.founder, founder);
    }
}
