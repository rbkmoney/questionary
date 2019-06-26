package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.PropertyInfoDao;
import com.rbkmoney.questionary.domain.tables.pojos.PropertyInfo;
import com.rbkmoney.questionary.domain.tables.records.PropertyInfoRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<Query> queries = propertyInfoList.stream()
                .map(propertyInfo -> {
                    PropertyInfoRecord propertyInfoRecord = getDslContext().newRecord(PROPERTY_INFO, propertyInfo);
                    return getDslContext().insertInto(PROPERTY_INFO).set(propertyInfoRecord);
                })
                .collect(Collectors.toList());
        batchExecute(queries);
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
