package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.InternationalLegalEntityQuestionaryDao;
import com.rbkmoney.questionary.domain.tables.pojos.InternationalLegalEntityQuestionary;
import com.rbkmoney.questionary.domain.tables.records.InternationalLegalEntityQuestionaryRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static com.rbkmoney.questionary.domain.Tables.INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY;

@Component
public class InternationalLegalEntityQuestionaryDaoImpl extends AbstractGenericDao
        implements InternationalLegalEntityQuestionaryDao {

    private final RecordRowMapper<InternationalLegalEntityQuestionary> internationalLegalEntityRowMapper;

    public InternationalLegalEntityQuestionaryDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.internationalLegalEntityRowMapper = new RecordRowMapper<>(
                INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY,
                InternationalLegalEntityQuestionary.class
        );
    }

    @Override
    public Long save(InternationalLegalEntityQuestionary internationalBankInfo) {
        InternationalLegalEntityQuestionaryRecord record = getDslContext()
                .newRecord(INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY, internationalBankInfo);
        Query query = getDslContext()
                .insertInto(INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY)
                .set(record)
                .returning(INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public InternationalLegalEntityQuestionary getById(Long id) {
        Query query = getDslContext()
                .selectFrom(INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY)
                .where(INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY.ID.eq(id));
        return fetchOne(query, internationalLegalEntityRowMapper);
    }

    @Override
    public InternationalLegalEntityQuestionary getByExtId(Long extId) {
        Query query = getDslContext()
                .selectFrom(INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY)
                .where(INTERNATIONAL_LEGAL_ENTITY_QUESTIONARY.EXT_ID.eq(extId));
        return fetchOne(query, internationalLegalEntityRowMapper);
    }
}
