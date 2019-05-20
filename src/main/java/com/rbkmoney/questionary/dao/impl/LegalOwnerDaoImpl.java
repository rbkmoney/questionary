package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.LegalOwnerDao;
import com.rbkmoney.questionary.domain.tables.pojos.LegalOwner;
import com.rbkmoney.questionary.domain.tables.records.LegalOwnerRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static com.rbkmoney.questionary.domain.Tables.LEGAL_OWNER;

@Component
public class LegalOwnerDaoImpl extends AbstractGenericDao implements LegalOwnerDao {

    private final RecordRowMapper<LegalOwner> legalOwnerRecordRowMapper;

    public LegalOwnerDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.legalOwnerRecordRowMapper = new RecordRowMapper<>(LEGAL_OWNER, LegalOwner.class);
    }

    @Override
    public Long save(LegalOwner legalOwner) {
        LegalOwnerRecord legalOwnerRecord = getDslContext().newRecord(LEGAL_OWNER, legalOwner);
        Query query = getDslContext().insertInto(LEGAL_OWNER).set(legalOwnerRecord).returning(LEGAL_OWNER.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public LegalOwner getById(Long id) {
        Query query = getDslContext().selectFrom(LEGAL_OWNER)
                .where(LEGAL_OWNER.ID.eq(id));
        return fetchOne(query, legalOwnerRecordRowMapper);
    }
}
