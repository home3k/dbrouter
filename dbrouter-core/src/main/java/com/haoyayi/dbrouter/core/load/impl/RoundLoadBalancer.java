/*
 * Baidu.com Inc.
 * Copyright (c) 2000-2012 All Rights Reserved.
 */
package com.haoyayi.dbrouter.core.load.impl;

import com.haoyayi.dbrouter.core.load.LoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One concrete {@link com.baidu.fengchao.adcore.db.datasource.LoadBalancer}
 * using polling algorithm.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class RoundLoadBalancer implements LoadBalancer {

    private static Logger LOG = LoggerFactory.getLogger(RoundLoadBalancer.class);

    private int current = 0;


    public Integer getNext(int N) {
        int idx = current % N;
        ++current;
        LOG.debug("Using polling read datasource.");
        return idx;
    }

}
