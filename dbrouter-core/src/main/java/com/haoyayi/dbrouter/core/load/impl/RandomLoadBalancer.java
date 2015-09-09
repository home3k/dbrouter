/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.load.impl;

import com.haoyayi.dbrouter.core.load.LoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author sunkai
 */
public class RandomLoadBalancer implements LoadBalancer {

    private static Logger LOG = LoggerFactory
            .getLogger(RandomLoadBalancer.class);

    protected Random randomprovider = new Random();

    public Integer getNext(int N) {
        LOG.debug("Use random read datasource.");
        return randomprovider.nextInt(N);
    }

}
