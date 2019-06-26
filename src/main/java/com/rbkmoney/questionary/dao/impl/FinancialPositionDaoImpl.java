package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.FinancialPositionDao;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import com.rbkmoney.questionary.domain.tables.records.FinancialPositionRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static com.rbkmoney.questionary.domain.Tables.FINANCIAL_POSITION;

@Component
public class FinancialPositionDaoImpl extends AbstractGenericDao implements FinancialPositionDao {

    private final RecordRowMapper<FinancialPosition> financialPositionRecordRowMapper;

    public FinancialPositionDaoImpl(DataSource dataSource) {
        super(dataSource);
        financialPositionRecordRowMapper = new RecordRowMapper<>(FINANCIAL_POSITION, FinancialPosition.class);
    }

    @Override
    public Long save(FinancialPosition financialPosition) {
        FinancialPositionRecord financialPositionRecord = getDslContext().newRecord(FINANCIAL_POSITION, financialPosition);
        Query query = getDslContext().insertInto(FINANCIAL_POSITION).set(financialPositionRecord).returning(FINANCIAL_POSITION.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveAll(List<FinancialPosition> financialPositionList) {
        final List<Query> queries = financialPositionList.stream()
                .map(financialPosition -> {
                    FinancialPositionRecord financialPositionRecord = getDslContext().newRecord(FINANCIAL_POSITION, financialPosition);
                    return getDslContext().insertInto(FINANCIAL_POSITION).set(financialPositionRecord);
                })
                .collect(Collectors.toList());
        batchExecute(queries);
    }

    @Override
    public FinancialPosition getById(Long id) {
        Query query = getDslContext().selectFrom(FINANCIAL_POSITION)
                .where(FINANCIAL_POSITION.ID.eq(id));
        return fetchOne(query, financialPositionRecordRowMapper);
    }

    @Override
    public List<FinancialPosition> getByAdditionalInfoId(Long id) {
        Query query = getDslContext().selectFrom(FINANCIAL_POSITION)
                .where(FINANCIAL_POSITION.ADDITIONAL_INFO_ID.eq(id));
        return fetch(query, financialPositionRecordRowMapper);
    }
}
