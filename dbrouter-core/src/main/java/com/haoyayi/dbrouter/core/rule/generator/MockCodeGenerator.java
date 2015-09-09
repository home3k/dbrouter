/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.rule.generator;

/**
 * @author home3k (sunkai@51haoyayi.com)
 */
public class MockCodeGenerator implements CodeGenerator {

    @Override
    public String getDatabaseCode(Object key) {
        return "";
    }

    @Override
    public String getTableCode(Object key) {
        return "";
    }
}
