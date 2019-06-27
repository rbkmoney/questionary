package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.FounderDao;
import com.rbkmoney.questionary.domain.tables.pojos.Founder;
import com.rbkmoney.questionary.domain.tables.records.FounderRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<Query> queries = founderList.stream()
                .map(founder -> {
                    final FounderRecord founderRecord = getDslContext().newRecord(FOUNDER, founder);
                    return getDslContext().insertInto(FOUNDER)
                            .set(founderRecord);
                })
                .collect(Collectors.toList());
        batchExecute(queries);
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
