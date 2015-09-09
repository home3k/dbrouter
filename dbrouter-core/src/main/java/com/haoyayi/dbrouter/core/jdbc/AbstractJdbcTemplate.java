/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.jdbc;

import com.haoyayi.dbrouter.core.context.RWContextHolder;
import com.haoyayi.dbrouter.core.exception.DbRouterException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Wrap the jdbcTemplate.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class AbstractJdbcTemplate extends JdbcTemplate {

    private String READ_SQL_PREFIX = "select";

    private String getProviderSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        } else {
            return null;
        }
    }

    private String checkOperationType(String sql) {
        String trimedSQL = sql.trim().toLowerCase();
        if (trimedSQL.startsWith(READ_SQL_PREFIX)) {
            return RWContextHolder.TYPE_READ;
        } else {
            return RWContextHolder.TYPE_WRITE;
        }
    }

    private void before(String sqlType) {
        if (!RWContextHolder.hasSetRwType()) {
            RWContextHolder.setContextRwType(sqlType, true);
        }
    }

    private void after() {
        if (!RWContextHolder.hasSetRwType()) {
            RWContextHolder.clearContextRwType();
        }
    }

    @Override
    public <T> T execute(StatementCallback<T> action) throws DataAccessException {
        try {
            String sql = getProviderSql(action);
            if (sql != null)
                before(checkOperationType(sql));
            else
                throw new DbRouterException("The sql is null, pls configure it.");
            return super.execute(action);
        } finally {
            after();
        }
    }

    @Override
    public void execute(String sql) throws DataAccessException {
        try {
            before(checkOperationType(sql));
            super.execute(sql);
        } finally {
            after();
        }
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, rse);
        } finally {
            after();
        }
    }

    @Override
    public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            super.query(sql, rch);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql) throws DataAccessException {

        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForMap(sql);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, requiredType);
        } finally {
            after();
        }
    }

    @Override
    public long queryForLong(String sql) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForLong(sql);
        } finally {
            after();
        }
    }

    @Override
    public int queryForInt(String sql) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForInt(sql);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForList(sql, elementType);
        } finally {
            after();
        }
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForList(sql);
        } finally {
            after();
        }
    }

    @Override
    public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForRowSet(sql);
        } finally {
            after();
        }
    }

    @Override
    public int update(String sql) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.update(sql);
        } finally {
            after();
        }
    }

    @Override
    public int[] batchUpdate(String[] sql) throws DataAccessException {

        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.batchUpdate(sql);
        } finally {
            after();
        }
    }

    @Override
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws DataAccessException {
        try {
            String sql = getProviderSql(psc);
            if (sql != null)
                before(checkOperationType(sql));
            else
                throw new DbRouterException("The sql is null, pls configure it.");
            return super.execute(psc, action);
        } finally {
            after();
        }
    }

    @Override
    public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.execute(sql, action);
        } finally {
            after();
        }
    }

    @Override
    public <T> T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(psc, pss, rse);
        } finally {
            after();
        }
    }

    @Override
    public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(psc, rse);
        } finally {
            after();
        }
    }

    @Override
    public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, pss, rse);
        } finally {
            after();
        }
    }

    @Override
    public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, args, argTypes, rse);
        } finally {
            after();
        }
    }

    @Override
    public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, args, rse);
        } finally {
            after();
        }
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, rse, args);
        } finally {
            after();
        }
    }

    @Override
    public void query(PreparedStatementCreator psc, RowCallbackHandler rch) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            super.query(psc, rch);
        } finally {
            after();
        }
    }

    @Override
    public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            super.query(sql, pss, rch);
        } finally {
            after();
        }
    }

    @Override
    public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            super.query(sql, args, argTypes, rch);
        } finally {
            after();
        }
    }

    @Override
    public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            super.query(sql, args, rch);
        } finally {
            after();
        }
    }

    @Override
    public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            super.query(sql, rch, args);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(psc, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, pss, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, args, argTypes, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, args, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.query(sql, rowMapper, args);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, args, argTypes, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, args, rowMapper);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, rowMapper, args);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, args, argTypes, requiredType);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, args, requiredType);
        } finally {
            after();
        }
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForObject(sql, requiredType, args);
        } finally {
            after();
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForMap(sql, args, argTypes);
        } finally {
            after();
        }
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForMap(sql, args);
        } finally {
            after();
        }
    }

    @Override
    public long queryForLong(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForLong(sql, args, argTypes);
        } finally {
            after();
        }
    }

    @Override
    public long queryForLong(String sql, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForLong(sql, args);
        } finally {
            after();
        }
    }

    @Override
    public int queryForInt(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForInt(sql, args, argTypes);
        } finally {
            after();
        }
    }

    @Override
    public int queryForInt(String sql, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForInt(sql, args);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForList(sql, args, argTypes, elementType);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForList(sql, args, elementType);
        } finally {
            after();
        }
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForList(sql, elementType, args);
        } finally {
            after();
        }
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForList(sql, args, argTypes);
        } finally {
            after();
        }
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForList(sql, args);
        } finally {
            after();
        }
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForRowSet(sql, args, argTypes);
        } finally {
            after();
        }
    }

    @Override
    public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_READ);
            return super.queryForRowSet(sql, args);
        } finally {
            after();
        }
    }

    @Override
    protected int update(PreparedStatementCreator psc, PreparedStatementSetter pss) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.update(psc, pss);
        } finally {
            after();
        }
    }

    @Override
    public int update(PreparedStatementCreator psc) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.update(psc);
        } finally {
            after();
        }
    }

    @Override
    public int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.update(psc, generatedKeyHolder);
        } finally {
            after();
        }
    }

    @Override
    public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.update(sql, pss);
        } finally {
            after();
        }
    }

    @Override
    public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.update(sql, args, argTypes);
        } finally {
            after();
        }
    }

    @Override
    public int update(String sql, Object... args) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.update(sql, args);
        } finally {
            after();
        }
    }

    @Override
    public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.batchUpdate(sql, pss);
        } finally {
            after();
        }
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.batchUpdate(sql, batchArgs);
        } finally {
            after();
        }
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.batchUpdate(sql, batchArgs, argTypes);
        } finally {
            after();
        }
    }

    @Override
    public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs, int batchSize, ParameterizedPreparedStatementSetter<T> pss) {
        try {
            before(RWContextHolder.TYPE_WRITE);
            return super.batchUpdate(sql, batchArgs, batchSize, pss);
        } finally {
            after();
        }
    }

    @Override
    public <T> T execute(CallableStatementCreator csc, CallableStatementCallback<T> action) throws DataAccessException {
        try {
            String sql = getProviderSql(csc);
            if (sql != null)
                before(checkOperationType(sql));
            else
                throw new DbRouterException("The sql is null, pls configure it.");
            return super.execute(csc, action);
        } finally {
            after();
        }
    }

    @Override
    public <T> T execute(String callString, CallableStatementCallback<T> action) throws DataAccessException {
        try {
            before(checkOperationType(callString));
            return super.execute(callString, action);
        } finally {
            after();
        }
    }
}
