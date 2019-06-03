package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.FinancialPositionDao;
import com.rbkmoney.questionary.domain.tables.pojos.FinancialPosition;
import com.rbkmoney.questionary.domain.tables.records.FinancialPositionRecord;
import org.jooq.Query;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

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
        String sql = "INSERT INTO qs.financial_position (additional_info_id, type, description)" +
                " VALUES (?, ?::qs.financial_pos_type, ?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                FinancialPosition financialPosition = financialPositionList.get(i);
                ps.setLong(1, financialPosition.getAdditionalInfoId());
                ps.setObject(2, financialPosition.getType().name());
                ps.setString(3, financialPosition.getDescription());
            }

            @Override
            public int getBatchSize() {
                return financialPositionList.size();
            }
        });
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
