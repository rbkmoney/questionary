package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.HeadDao;
import com.rbkmoney.questionary.domain.tables.pojos.Head;
import com.rbkmoney.questionary.domain.tables.records.HeadRecord;
import org.jooq.Query;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.rbkmoney.questionary.domain.Tables.HEAD;

@Component
public class HeadDaoImpl extends AbstractGenericDao implements HeadDao {

    private final RecordRowMapper<Head> headRecordRowMapper;

    public HeadDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.headRecordRowMapper = new RecordRowMapper<>(HEAD, Head.class);
    }

    @Override
    public Long save(Head head) {
        HeadRecord headRecord = getDslContext().newRecord(HEAD, head);
        Query query = getDslContext().insertInto(HEAD).set(headRecord).returning(HEAD.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveAll(List<Head> headList) {
        String sql = "INSERT INTO qs.head (questionary_id, first_name, second_name, middle_name, inn, position)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Head founder = headList.get(i);
                ps.setLong(1, founder.getQuestionaryId());
                ps.setString(2, founder.getFirstName());
                ps.setString(3, founder.getSecondName());
                ps.setString(4, founder.getMiddleName());
                ps.setString(5, founder.getInn());
                ps.setString(6, founder.getPosition());
            }

            @Override
            public int getBatchSize() {
                return headList.size();
            }
        });
    }

    @Override
    public Head getById(Long id) {
        Query query = getDslContext().selectFrom(HEAD).where(HEAD.ID.eq(id));
        return fetchOne(query, headRecordRowMapper);
    }

    @Override
    public List<Head> getByQuestionaryId(Long id) {
        Query query = getDslContext().selectFrom(HEAD).where(HEAD.QUESTIONARY_ID.eq(id));
        return fetch(query, headRecordRowMapper);
    }
}
