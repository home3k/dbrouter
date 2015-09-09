/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.rule.generator;

/**
 * Wrapping database/table/table-suffix number generating algorithm.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public interface CodeGenerator {

    /**
     * Generating database code algorithm by key.
     */
    String getDatabaseCode(Object key);

    /**
     * Generating table code algorithm by key.
     */
    String getTableCode(Object key);

}
