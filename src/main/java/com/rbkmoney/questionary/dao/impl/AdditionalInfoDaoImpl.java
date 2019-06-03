package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.AdditionalInfoDao;
import com.rbkmoney.questionary.domain.tables.pojos.AdditionalInfo;
import com.rbkmoney.questionary.domain.tables.records.AdditionalInfoRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static com.rbkmoney.questionary.domain.Tables.ADDITIONAL_INFO;

@Component
public class AdditionalInfoDaoImpl extends AbstractGenericDao implements AdditionalInfoDao {

    private final RecordRowMapper<AdditionalInfo> additionalInfoRecordRowMapper;

    public AdditionalInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.additionalInfoRecordRowMapper = new RecordRowMapper<>(ADDITIONAL_INFO, AdditionalInfo.class);
    }

    @Override
    public Long save(AdditionalInfo additionalInfo) {
        AdditionalInfoRecord additionalInfoRecord = getDslContext().newRecord(ADDITIONAL_INFO, additionalInfo);
        Query query = getDslContext().insertInto(ADDITIONAL_INFO).set(additionalInfoRecord).returning(ADDITIONAL_INFO.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public AdditionalInfo getById(Long id) {
        Query query = getDslContext().selectFrom(ADDITIONAL_INFO)
                .where(ADDITIONAL_INFO.ID.eq(id));
        return fetchOne(query, additionalInfoRecordRowMapper);
    }
}
