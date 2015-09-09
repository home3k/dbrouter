/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.load;

import com.haoyayi.dbrouter.core.load.impl.RandomLoadBalancer;
import com.haoyayi.dbrouter.core.load.impl.RoundLoadBalancer;

/**
 * {@link com.haoyayi.dbrouter.core.load.LoadBalancer} Factory
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public abstract class LoadBalancerFactory {

    /**
     * Get concrete {@link com.haoyayi.dbrouter.core.load.LoadBalancer}
     *
     * @param type
     * @return LoadBalancer
     */
    public static LoadBalancer getLoadBalancer(LoadType type) {
        switch (type) {
            case BALANCE_RANDOM:
                return new RandomLoadBalancer();
            case BALANCE_ROUND_ROBIN:
                return new RoundLoadBalancer();
            default:
                return new RandomLoadBalancer();
        }
    }
}
