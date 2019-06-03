package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.PropertyInfoDao;
import com.rbkmoney.questionary.domain.tables.pojos.PropertyInfo;
import com.rbkmoney.questionary.domain.tables.records.PropertyInfoRecord;
import org.jooq.Query;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.rbkmoney.questionary.domain.Tables.PROPERTY_INFO;

@Component
public class PropertyInfoDaoImpl extends AbstractGenericDao implements PropertyInfoDao {

    private final RecordRowMapper<PropertyInfo> propertyInfoRecordRowMapper;

    public PropertyInfoDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.propertyInfoRecordRowMapper = new RecordRowMapper<>(PROPERTY_INFO, PropertyInfo.class);
    }

    @Override
    public Long save(PropertyInfo propertyInfo) {
        PropertyInfoRecord propertyInfoRecord = getDslContext().newRecord(PROPERTY_INFO, propertyInfo);
        Query query = getDslContext().insertInto(PROPERTY_INFO).set(propertyInfoRecord).returning(PROPERTY_INFO.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveAll(List<PropertyInfo> propertyInfoList) {
        String sql = "INSERT INTO qs.property_info (questionary_id, description)" +
                " VALUES (?, ?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PropertyInfo propertyInfo = propertyInfoList.get(i);
                ps.setLong(1, propertyInfo.getQuestionaryId());
                ps.setString(2, propertyInfo.getDescription());
            }

            @Override
            public int getBatchSize() {
                return propertyInfoList.size();
            }
        });
    }

    @Override
    public PropertyInfo getById(Long id) {
        Query query = getDslContext().selectFrom(PROPERTY_INFO).where(PROPERTY_INFO.ID.eq(id));
        return fetchOne(query, propertyInfoRecordRowMapper);
    }

    @Override
    public List<PropertyInfo> getByQuestionaryId(Long questionaryId) {
        Query query = getDslContext().selectFrom(PROPERTY_INFO).where(PROPERTY_INFO.QUESTIONARY_ID.eq(questionaryId));
        return fetch(query, propertyInfoRecordRowMapper);
    }
}
