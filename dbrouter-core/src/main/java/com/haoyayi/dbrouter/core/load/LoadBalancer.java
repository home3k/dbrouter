/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.load;

/**
 * Load Balancer
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public interface LoadBalancer {

    /**
     * Get number
     *
     * @param N
     * @return Integer
     * @author sunkai
     * @date 2012-5-31
     */
    Integer getNext(int N);

}
