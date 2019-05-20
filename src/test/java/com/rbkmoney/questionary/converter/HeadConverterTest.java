package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.domain.tables.pojos.Head;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HeadConverterTest {

    private HeadConverter headConverter = new HeadConverter();

    private Head head;

    private com.rbkmoney.questionary.Head thriftHead;

    @Before
    public void setUp() throws Exception {
        head = EnhancedRandom.random(Head.class);
        head.setId(null);
        head.setQuestionaryId(null);
    }

    @Test
    public void convertTest() {
        final com.rbkmoney.questionary.Head thriftHead = headConverter.convertToThrift(this.head);
        Assert.assertEquals(head, headConverter.convertFromThrift(thriftHead));
    }
}
