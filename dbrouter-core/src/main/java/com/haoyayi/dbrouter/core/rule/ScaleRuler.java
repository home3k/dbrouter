/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.rule;

import com.haoyayi.dbrouter.core.context.KeyContextHolder;
import com.haoyayi.dbrouter.core.rule.generator.CodeGenerator;
import com.haoyayi.dbrouter.core.rule.generator.MockCodeGenerator;

/**
 * Get databaseNo,tableNo,tableName by request. E.g. uid etc.
 * The generating algorithm is using CodeGenerator
 * {@link com.haoyayi.dbrouter.core.rule.generator.CodeGenerator}
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class ScaleRuler {

    private CodeGenerator codeGenerator = new MockCodeGenerator();

    /**
     * Get Table code by key(define in annotation.)
     *
     * @return
     */
    public String getTableNo() {
        return getTableNo(KeyContextHolder.getContextKey());
    }

    /**
     * Get Table code by key
     *
     * @param key
     * @return
     */
    public String getTableNo(Object key) {
        return codeGenerator.getTableCode(key);
    }

    /**
     * Get TableName by key
     *
     * @param tablename
     * @param key
     * @return tablename like "wmkq.dentist"
     */
    public String getTableName(final String tablename, Object key, String seperator) {
        String tableNo = getTableNo(key);
        return tablename + seperator + tableNo;
    }

    /**
     * Get TableName by key
     *
     * @param tablename
     * @return tablename like "wmkq.dentist"
     */
    public String getTableName(final String tablename, String seperator) {
        return getTableName(tablename, KeyContextHolder.getContextKey(), seperator);
    }

    /**
     * Get TableName by key
     *
     * @param tablename
     * @param key
     * @return
     */
    public String getTableName(final String tablename, Object key) {
        return getTableName(tablename, key, "");
    }

    /**
     * Get TableName by key
     *
     * @param tablename
     * @return
     */
    public String getTableName(final String tablename) {
        return getTableName(tablename, KeyContextHolder.getContextKey(), "");
    }

    /**
     * get DB Code by key
     *
     * @param key
     * @return DB Code like "010" or "111"
     */
    public String getDatabaseNo(Object key) {
        return codeGenerator.getDatabaseCode(key);
    }

    /**
     * get DB Code by key
     *
     * @return
     */
    public String getDatabaseNo() {
        return getDatabaseNo(KeyContextHolder.getContextKey());
    }

}
