/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.jdbc;

import com.haoyayi.dbrouter.core.context.RWContextHolder;
import com.haoyayi.dbrouter.core.datasource.VirtualDataSource;
import com.haoyayi.dbrouter.core.exception.DbRouterException;

import javax.sql.DataSource;

/**
 * A helpless implementation of jdbcTemplate.
 * <p/>
 * The jdbcTemplate using the VirtualDataSource
 * {@link com.haoyayi.dbrouter.core.datasource.VirtualDataSource} to route
 * the actual datasource.
 * But it uses datasource reference to verify the necessity of creating new
 * connection during a transaction in spring jdbc implementation.
 *
 * @author home3k (sunkai@51haoyayi.com)
 * @see{DataSourceUtils} so we have to override the getDataSource() method sadly.
 */
public class DbRouterJdbcTemplate extends AbstractJdbcTemplate {

    /**
     * @see org.springframework.jdbc.support.JdbcAccessor#getDataSource()
     */
    @Override
    public DataSource getDataSource() {
        DataSource virtualDataSource = super.getDataSource();
        if (!(virtualDataSource instanceof VirtualDataSource)) {
            throw new DbRouterException(
                    "You have to config VirtualDataSource for JdbcTemplate!");
        }
        String rwType = RWContextHolder.getContextRwType();
        if (null == rwType) {
            return virtualDataSource;
        }
        if (rwType.equals(RWContextHolder.TYPE_WRITE)) {
            return virtualDataSource;
        } else {
            return ((VirtualDataSource) virtualDataSource).gainTargetDataSource();
        }
    }
}
