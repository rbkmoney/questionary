package com.rbkmoney.questionary.dao.impl;

import com.rbkmoney.dao.impl.AbstractGenericDao;
import com.rbkmoney.mapper.RecordRowMapper;
import com.rbkmoney.questionary.dao.HeadDao;
import com.rbkmoney.questionary.domain.tables.pojos.Head;
import com.rbkmoney.questionary.domain.tables.records.HeadRecord;
import org.jooq.Query;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<Query> queries = headList.stream()
                .map(head -> {
                    final HeadRecord headRecord = getDslContext().newRecord(HEAD, head);
                    return getDslContext().insertInto(HEAD).set(headRecord);
                })
                .collect(Collectors.toList());
        batchExecute(queries);
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
