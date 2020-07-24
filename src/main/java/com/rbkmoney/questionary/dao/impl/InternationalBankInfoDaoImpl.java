package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.InternationalBankInfoDao;
import com.rbkmoney.questionary.domain.tables.pojos.InternationalBankInfo;
import com.rbkmoney.questionary.domain.tables.records.InternationalBankInfoRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static com.rbkmoney.questionary.domain.Tables.INTERNATIONAL_BANK_INFO;

@Component
public class InternationalBankInfoDaoImpl extends AbstractGenericDao implements InternationalBankInfoDao {

    private final RecordRowMapper<InternationalBankInfo> bankInfoRecordRowMapper;

    public InternationalBankInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.bankInfoRecordRowMapper = new RecordRowMapper<>(INTERNATIONAL_BANK_INFO, InternationalBankInfo.class);
    }

    @Override
    public Long save(InternationalBankInfo internationalBankInfo) {
        InternationalBankInfoRecord record = getDslContext().newRecord(INTERNATIONAL_BANK_INFO, internationalBankInfo);
        Query query = getDslContext()
                .insertInto(INTERNATIONAL_BANK_INFO)
                .set(record)
                .returning(INTERNATIONAL_BANK_INFO.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public InternationalBankInfo getById(Long id) {
        Query query = getDslContext()
                .selectFrom(INTERNATIONAL_BANK_INFO)
                .where(INTERNATIONAL_BANK_INFO.ID.eq(id));
        return fetchOne(query, bankInfoRecordRowMapper);
    }

    @Override
    public InternationalBankInfo getByExtId(Long extId) {
        Query query = getDslContext()
                .selectFrom(INTERNATIONAL_BANK_INFO)
                .where(INTERNATIONAL_BANK_INFO.EXT_ID.eq(extId));
        return fetchOne(query, bankInfoRecordRowMapper);
    }
}
