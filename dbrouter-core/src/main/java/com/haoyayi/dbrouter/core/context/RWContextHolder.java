/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.context;

/**
 * Read/Write ThreadLocal Holder.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class RWContextHolder {

    /**
     * Read/Write Type.
     */
    public static final String TYPE_READ = "READ";

    public static final String TYPE_WRITE = "WRITE";

    /**
     * ThreadLocal Holder
     */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    private static final ThreadLocal<String> autoHolder = new ThreadLocal<String>();


    public static void setContextRwType(String type) {
        setContextRwType(type, false);
    }

    public static void setContextRwType(String type, boolean auto) {
        contextHolder.set(type);
        if (auto) {
            autoHolder.set("auto");
        }
    }


    public static String getContextRwType() {
        return (String) contextHolder.get();
    }

    public static boolean hasSetRwType() {
        return (getContextRwType() != null && autoHolder.get() == null);
    }

    public static void clearContextRwType() {
        contextHolder.remove();
        autoHolder.remove();
    }
}
