package com.rbkmoney.questionary.converter;

import com.rbkmoney.questionary.RussianIndividualEntity;
import com.rbkmoney.questionary.domain.enums.QuestionaryEntityType;
import com.rbkmoney.questionary.domain.tables.pojos.IndividualEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.LegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import com.rbkmoney.questionary.manage.QuestionaryParams;
import com.rbkmoney.questionary.model.IndividualEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.LegalEntityQuestionaryHolder;
import com.rbkmoney.questionary.model.QuestionaryHolder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuestionaryConverterTest {

    private QuestionaryParamsConverter questionaryParamsConverter = new QuestionaryParamsConverter();

    private Questionary questionary;

    @Before
    public void setUp() throws Exception {
        questionary = EnhancedRandom.random(Questionary.class);
        questionary.setId(null);
        questionary.setVersion(null);
    }

    @Test
    public void convertIndividualEntityTest() {
        questionary.setType(QuestionaryEntityType.individual);
        IndividualEntityQuestionary individualEntityQuestionary = EnhancedRandom.random(IndividualEntityQuestionary.class);
        individualEntityQuestionary.setId(null);
        final IndividualEntityQuestionaryHolder individualEntityQuestionaryHolder = IndividualEntityQuestionaryHolder.builder()
                .questionary(questionary)
                .individualEntityQuestionary(individualEntityQuestionary)
                .build();
        final QuestionaryHolder questionaryHolderConvert = QuestionaryHolder.builder()
                .questionary(questionary)
                .individualEntityQuestionaryHolder(individualEntityQuestionaryHolder)
                .build();
        final QuestionaryParams questionaryParams = questionaryParamsConverter.convertToThrift(questionaryHolderConvert);
        final QuestionaryHolder questionaryHolder = questionaryParamsConverter.convertFromThrift(questionaryParams);

        Assert.assertEquals(questionaryHolder.getQuestionary(), questionaryHolder.getQuestionary());
        Assert.assertEquals(questionaryHolder.getIndividualEntityQuestionaryHolder().getIndividualEntityQuestionary(),
                questionaryHolder.getIndividualEntityQuestionaryHolder().getIndividualEntityQuestionary());
    }

    @Test
    public void convertLegalEntityTest() {
        questionary.setType(QuestionaryEntityType.legal);
        final LegalEntityQuestionary legalEntityQuestionary = EnhancedRandom.random(LegalEntityQuestionary.class);
        legalEntityQuestionary.setId(null);
        final LegalEntityQuestionaryHolder legalEntityQuestionaryHolder = LegalEntityQuestionaryHolder.builder()
                .questionary(questionary)
                .legalEntityQuestionary(legalEntityQuestionary)
                .build();
        final QuestionaryHolder questionaryHolderConvert = QuestionaryHolder.builder()
                .questionary(questionary)
                .legalEntityQuestionaryHolder(legalEntityQuestionaryHolder)
                .build();
        final QuestionaryParams questionaryParams = questionaryParamsConverter.convertToThrift(questionaryHolderConvert);
        final QuestionaryHolder questionaryHolder = questionaryParamsConverter.convertFromThrift(questionaryParams);

        Assert.assertEquals(questionaryHolder.getQuestionary(), questionaryHolder.getQuestionary());
        Assert.assertEquals(questionaryHolder.getLegalEntityQuestionaryHolder().getLegalEntityQuestionary(),
                questionaryHolder.getLegalEntityQuestionaryHolder().getLegalEntityQuestionary());

    }
}
