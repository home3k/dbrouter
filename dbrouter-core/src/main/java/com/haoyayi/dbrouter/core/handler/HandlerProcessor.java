/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.handler;

import com.haoyayi.dbrouter.core.context.KeyContextHolder;
import com.haoyayi.dbrouter.core.context.RWContextHolder;
import com.haoyayi.dbrouter.core.exception.DbRouterException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * Handler processor.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class HandlerProcessor {

    /**
     * It's responsible for set threadLocal context of userid
     *
     * @param template
     * @param joinPoint void
     */
    public void setKey(String template, ProceedingJoinPoint joinPoint) {
        if (null == template) {
            // Nothing to do. Maybe not set.
            return;
        }

        Object params[] = joinPoint.getArgs();
        Object param = null;
        if (template.contains(".")) {
            Object holder = params[getIndexByParam(template.substring(0,
                    template.indexOf(".")))];
            String methodStr = template.substring(template.indexOf(".") + 1);
            Method method = getMethod(getGetterMethod(methodStr), holder);
            if (null != method) {
                param = ReflectionUtils.invokeMethod(method, holder);
            }
        } else {
            param = params[getIndexByParam(template)];
        }

        if (null == param) {
            throw new DbRouterException("Annotation params error.");
        }

        KeyContextHolder.setContextKey(param);

    }

    /**
     * It's responsible for set threadLocal context of database operation type
     *
     * @param sqlType void
     */
    public void setRwType(String sqlType) {
        if (!StringUtils.isEmpty(sqlType)) {
            if (sqlType.equals(RWContextHolder.TYPE_READ)) {
                RWContextHolder.setContextRwType(RWContextHolder.TYPE_READ);
            } else if (sqlType.equals(RWContextHolder.TYPE_WRITE)) {
                RWContextHolder.setContextRwType(RWContextHolder.TYPE_WRITE);
            } else {
                RWContextHolder.setContextRwType(RWContextHolder.TYPE_READ);
            }
        }
    }

    /**
     * Get executing method
     *
     * @param methodName
     * @param object
     * @return Method
     */
    public Method getMethod(String methodName, Object object) {
        Method[] methods = object.getClass().getMethods();
        Method method = null;
        // @Around method. Accurate match.
        for (int i = 0, len = methods.length; i < len; i++) {
            if (methodName.equals(methods[i].toString())) {
                method = methods[i];
                return method;
            }
        }
        // Getter method. Method name match
        for (int i = 0, len = methods.length; i < len; i++) {
            if (methods[i].toString().contains(methodName)) {
                method = methods[i];
                return method;
            }
        }
        return method;
    }

    /**
     * Generating Getter method.
     *
     * @param method
     * @return String
     */
    public String getGetterMethod(String method) {
        return "get" + method.substring(0, 1).toUpperCase()
                + method.substring(1);
    }

    /**
     * Get param index.
     *
     * @param param
     * @return int
     */
    public int getIndexByParam(String param) {
        String t = param
                .substring((param.indexOf("{") + 1), param.indexOf("}"));
        return Integer.parseInt(t);
    }

    /**
     * Clear the key context
     */
    public void clearContextKey() {
        KeyContextHolder.clearContextKey();
    }

    /**
     * Clear the r/w context
     */
    public void clearContextRw() {
        RWContextHolder.clearContextRwType();
    }
}
