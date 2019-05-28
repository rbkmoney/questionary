package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.IndividualEntity;
import com.rbkmoney.questionary.LegalEntity;
import com.rbkmoney.questionary.dao.QuestionaryDao;
import com.rbkmoney.questionary.dao.mapper.IndividualEntityQuestionaryMapper;
import com.rbkmoney.questionary.dao.mapper.LegalEntityQuestionaryMapper;
import com.rbkmoney.questionary.domain.tables.pojos.IndividualEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.LegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.pojos.Questionary;
import com.rbkmoney.questionary.domain.tables.records.IndividualEntityQuestionaryRecord;
import com.rbkmoney.questionary.domain.tables.records.LegalEntityQuestionaryRecord;
import com.rbkmoney.questionary.domain.tables.records.QuestionaryRecord;
import org.jooq.Query;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static com.rbkmoney.questionary.domain.Tables.*;

@Component
public class QuestionaryDaoImpl extends AbstractGenericDao implements QuestionaryDao {

    private final RowMapper<Questionary> questionaryRowMapper;

    private final RowMapper<IndividualEntityQuestionary> individualEntityQuestionaryRowMapper;

    private final RowMapper<LegalEntityQuestionary> legalEntityQuestionaryRowMapper;

    private final IndividualEntityQuestionaryMapper individualEntityQuestionaryMapper;

    private final LegalEntityQuestionaryMapper legalEntityQuestionaryMapper;

    @Autowired
    public QuestionaryDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.questionaryRowMapper = new RecordRowMapper<>(QUESTIONARY, Questionary.class);
        this.individualEntityQuestionaryRowMapper = new RecordRowMapper<>(
                INDIVIDUAL_ENTITY_QUESTIONARY,
                IndividualEntityQuestionary.class);
        this.legalEntityQuestionaryRowMapper = new RecordRowMapper<>(LEGAL_ENTITY_QUESTIONARY, LegalEntityQuestionary.class);
        this.individualEntityQuestionaryMapper = new IndividualEntityQuestionaryMapper();
        this.legalEntityQuestionaryMapper = new LegalEntityQuestionaryMapper();
    }

    @Override
    public Long saveQuestionary(Questionary questionary) {
        QuestionaryRecord questionaryRecord = getDslContext().newRecord(QUESTIONARY, questionary);
        Query query = getDslContext().insertInto(QUESTIONARY).set(questionaryRecord).returning(QUESTIONARY.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Questionary getQuestionaryById(Long questionaryId) {
        Query query = getDslContext().selectFrom(QUESTIONARY)
                .where(QUESTIONARY.ID.eq(questionaryId));
        return fetchOne(query, questionaryRowMapper);
    }

    @Override
    public Questionary getLatestQuestionary(String questionaryId) {
        Query query = getDslContext().selectFrom(QUESTIONARY)
                .where(QUESTIONARY.QUESTIONARY_ID.eq(questionaryId))
                .and(QUESTIONARY.VERSION.eq(
                        getDslContext().select(DSL.max(QUESTIONARY.VERSION))
                                .from(QUESTIONARY)
                ));
        return fetchOne(query, questionaryRowMapper);
    }

    @Override
    public Questionary getQuestionaryByIdAndVersion(String questionaryId, Long version) {
        Query query = getDslContext().selectFrom(QUESTIONARY)
                .where(QUESTIONARY.QUESTIONARY_ID.eq(questionaryId))
                .and(QUESTIONARY.VERSION.eq(version));
        return fetchOne(query, questionaryRowMapper);
    }

    @Override
    public Long saveIndividualEntity(IndividualEntityQuestionary questionary) {
        IndividualEntityQuestionaryRecord questionaryRecord = getDslContext()
                .newRecord(INDIVIDUAL_ENTITY_QUESTIONARY, questionary);
        Query query = getDslContext().insertInto(INDIVIDUAL_ENTITY_QUESTIONARY)
                .set(questionaryRecord).returning(INDIVIDUAL_ENTITY_QUESTIONARY.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public IndividualEntity getIndividualEntityById(Long individualEntityId) {
        Query query = getDslContext().selectFrom(INDIVIDUAL_ENTITY_QUESTIONARY
                .join(QUESTIONARY).on(INDIVIDUAL_ENTITY_QUESTIONARY.ID.eq(QUESTIONARY.ID)))
                .where(INDIVIDUAL_ENTITY_QUESTIONARY.ID.eq(individualEntityId));
        return fetchOne(query, individualEntityQuestionaryMapper);
    }

    @Override
    public IndividualEntityQuestionary getIndividualEntityQuestionaryById(Long id) {
        Query query = getDslContext().selectFrom(INDIVIDUAL_ENTITY_QUESTIONARY)
                .where(INDIVIDUAL_ENTITY_QUESTIONARY.ID.eq(id));
        return fetchOne(query, individualEntityQuestionaryRowMapper);

    }

    @Override
    public LegalEntityQuestionary getLegalEntityQuestionaryById(Long id) {
        Query query = getDslContext().selectFrom(LEGAL_ENTITY_QUESTIONARY)
                .where(LEGAL_ENTITY_QUESTIONARY.ID.eq(id));
        return fetchOne(query, legalEntityQuestionaryRowMapper);
    }

    @Override
    public Long saveLegalEntity(LegalEntityQuestionary questionary) {
        LegalEntityQuestionaryRecord questionaryRecord = getDslContext().newRecord(LEGAL_ENTITY_QUESTIONARY, questionary);
        Query query = getDslContext().insertInto(LEGAL_ENTITY_QUESTIONARY)
                .set(questionaryRecord).returning(LEGAL_ENTITY_QUESTIONARY.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public LegalEntity getLegalEntityById(Long questionaryId) {
        Query query = getDslContext().selectFrom(LEGAL_ENTITY_QUESTIONARY
                .join(QUESTIONARY).on(LEGAL_ENTITY_QUESTIONARY.ID.eq(QUESTIONARY.ID))
                .leftJoin(LEGAL_OWNER).on(LEGAL_ENTITY_QUESTIONARY.LEGAL_OWNER_ID.eq(LEGAL_OWNER.ID)))
                .where(LEGAL_ENTITY_QUESTIONARY.ID.eq(questionaryId));
        return fetchOne(query, legalEntityQuestionaryMapper);
    }

}
