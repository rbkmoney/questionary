package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.FounderDao;
import com.rbkmoney.questionary.domain.tables.pojos.Founder;
import com.rbkmoney.questionary.domain.tables.records.FounderRecord;
import org.jooq.Query;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.rbkmoney.questionary.domain.Tables.FOUNDER;

@Component
public class FounderDaoImpl extends AbstractGenericDao implements FounderDao {

    private final RecordRowMapper<Founder> founderRecordRowMapper;

    public FounderDaoImpl(DataSource dataSource) {
        super(dataSource);
        this.founderRecordRowMapper = new RecordRowMapper<>(FOUNDER, Founder.class);
    }

    @Override
    public Long save(Founder founder) {
        FounderRecord founderRecord = getDslContext().newRecord(FOUNDER, founder);
        Query query = getDslContext().insertInto(FOUNDER).set(founderRecord).returning(FOUNDER.ID);
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        execute(query, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveAll(List<Founder> founderList) {
        String sql = "INSERT INTO qs.founder (questionary_id, type, first_name, second_name, middle_name, inn, ogrn, full_name, country)" +
                " VALUES (?, ?::qs.founder_type, ?, ?, ?, ?, ?, ?, ?)";
        getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Founder founder = founderList.get(i);
                ps.setLong(1, founder.getQuestionaryId());
                ps.setString(2, founder.getType().name());
                ps.setString(3, founder.getFirstName());
                ps.setString(4, founder.getSecondName());
                ps.setString(5, founder.getMiddleName());
                ps.setString(6, founder.getInn());
                ps.setString(7, founder.getOgrn());
                ps.setString(8, founder.getFullName());
                ps.setString(9, founder.getCountry());
            }

            @Override
            public int getBatchSize() {
                return founderList.size();
            }
        });
    }

    @Override
    public Founder getById(Long id) {
        Query query = getDslContext().selectFrom(FOUNDER).where(FOUNDER.ID.eq(id));
        return fetchOne(query, founderRecordRowMapper);
    }

    @Override
    public List<Founder> getByQuestionaryId(Long id) {
        Query query = getDslContext().selectFrom(FOUNDER).where(FOUNDER.QUESTIONARY_ID.eq(id));
        return fetch(query, founderRecordRowMapper);
    }
}
