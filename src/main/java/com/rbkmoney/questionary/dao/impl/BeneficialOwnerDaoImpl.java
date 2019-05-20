package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.BeneficialOwnerDao;
import com.rbkmoney.questionary.domain.tables.pojos.BeneficialOwner;
import com.rbkmoney.questionary.domain.tables.records.BeneficialOwnerRecord;
import org.jooq.Query;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.rbkmoney.questionary.domain.Tables.BENEFICIAL_OWNER;

@Component
public class BeneficialOwnerDaoImpl extends AbstractGenericDao implements BeneficialOwnerDao {

    private final RecordRowMapper<BeneficialOwner> beneficialOwnerMapper;

    public BeneficialOwnerDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.beneficialOwnerMapper = new RecordRowMapper<>(BENEFICIAL_OWNER, BeneficialOwner.class);
    }

    @Override
    public Long save(BeneficialOwner beneficialOwner) {
        BeneficialOwnerRecord identityDocumentRecord = getDslContext().newRecord(BENEFICIAL_OWNER, beneficialOwner);
        Query query = getDslContext().insertInto(BENEFICIAL_OWNER).set(identityDocumentRecord).returning(BENEFICIAL_OWNER.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public BeneficialOwner getById(Long id) {
        Query query = getDslContext().selectFrom(BENEFICIAL_OWNER)
                .where(BENEFICIAL_OWNER.ID.eq(id));
        return fetchOne(query, beneficialOwnerMapper);
    }

    @Override
    public List<BeneficialOwner> getByQuestionaryId(Long questionaryId) {
        Query query = getDslContext().selectFrom(BENEFICIAL_OWNER)
                .where(BENEFICIAL_OWNER.QUESTIONARY_ID.eq(questionaryId));
        return fetch(query, beneficialOwnerMapper);
    }
}
