/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.handler;

import com.haoyayi.dbrouter.core.anno.KeyDesc;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author home3k (sunkai@51haoyayi.com)
 */
public class KeyDescHandler {

    private HandlerProcessor processor = new HandlerProcessor();

    /**
     * Proceed.
     *
     * @param joinPoint
     * @return
     * @throws Throwable Object
     */
    public Object adviceKey(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint
                .getSignature();
        Method targetMethod = methodSignature.getMethod();
        KeyDesc annotation = targetMethod
                .getAnnotation(KeyDesc.class);
        if (null != annotation) {
            return process(joinPoint, annotation);
        }

        Class[] params = targetMethod.getParameterTypes();
        if (0 == params.length) {
            return joinPoint.proceed();
        } else {
            annotation = new KeyDesc() {
                @Override
                public String key() {
                    return "";
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return KeyDesc.class;
                }
            };
            return process(joinPoint, annotation);

        }
    }

    /**
     * Process join point.
     *
     * @param joinPoint
     * @param annotation
     * @return
     * @throws Throwable Object
     */
    protected Object process(ProceedingJoinPoint joinPoint,
                             KeyDesc annotation) throws Throwable {
        processor.clearContextKey();
        String key = annotation.key();
        processor.setKey(key, joinPoint);
        Object result = joinPoint.proceed();
        processor.clearContextKey();
        return result;
    }

}
