/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.context;

/**
 * Key (for scale out.) ThreadLocal Holder.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class KeyContextHolder {

    /**
     * ThreadLocal Holder
     */
    private static final ThreadLocal<Object> contextHolder = new ThreadLocal<Object>();

    /**
     * Set Key TO ThreadLocal
     *
     * @param key void
     */
    public static void setContextKey(Object key) {
        contextHolder.set(key);
    }

    /**
     * Get Key FROM ThreadLocal
     *
     * @return Object
     */
    public static Object getContextKey() {
        return contextHolder.get();
    }

    /**
     * Has set scale key?
     * @return
     */
    public static boolean hasSetKey() {
        return getContextKey()!=null;
    }

    /**
     * Clear ThreadLocal
     */
    public static void clearContextKey() {
        contextHolder.remove();
    }

}
