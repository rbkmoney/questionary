package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.BusinessInfoDao;
import com.rbkmoney.questionary.domain.tables.pojos.BusinessInfo;
import com.rbkmoney.questionary.domain.tables.records.BusinessInfoRecord;
import org.jooq.Query;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.rbkmoney.questionary.domain.Tables.BUSINESS_INFO;

@Component
public class BusinessInfoDaoImpl extends AbstractGenericDao implements BusinessInfoDao {

    private final RecordRowMapper<BusinessInfo> businessInfoRecordRowMapper;

    public BusinessInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.businessInfoRecordRowMapper = new RecordRowMapper<>(BUSINESS_INFO, BusinessInfo.class);
    }

    @Override
    public Long save(BusinessInfo businessInfo) {
        BusinessInfoRecord additionalInfoRecord = getDslContext().newRecord(BUSINESS_INFO, businessInfo);
        Query query = getDslContext().insertInto(BUSINESS_INFO).set(additionalInfoRecord).returning(BUSINESS_INFO.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveAll(List<BusinessInfo> businessInfoList) {
        String sql = "INSERT INTO qs.business_info (additional_info_id, type, description)" +
                " VALUES (?, ?::qs.business_info_type, ?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                BusinessInfo businessInfo = businessInfoList.get(i);
                ps.setLong(1, businessInfo.getAdditionalInfoId());
                ps.setObject(2, businessInfo.getType().name());
                ps.setString(3, businessInfo.getDescription());
            }

            @Override
            public int getBatchSize() {
                return businessInfoList.size();
            }
        });
    }

    @Override
    public BusinessInfo getById(Long id) {
        Query query = getDslContext().selectFrom(BUSINESS_INFO)
                .where(BUSINESS_INFO.ID.eq(id));
        return fetchOne(query, businessInfoRecordRowMapper);
    }

    @Override
    public List<BusinessInfo> getByAdditionalInfoId(Long id) {
        Query query = getDslContext().selectFrom(BUSINESS_INFO)
                .where(BUSINESS_INFO.ADDITIONAL_INFO_ID.eq(id));
        return fetch(query, businessInfoRecordRowMapper);
    }
}
