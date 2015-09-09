/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */
package com.haoyayi.dbrouter.core.handler;

import com.haoyayi.dbrouter.core.anno.DBOperationDesc;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Aspect for Annotation
 * {@link com.haoyayi.dbrouter.core.anno.DBOperationDesc}.<br/>
 * It's
 * responsible for set threadLocal context of userid and database operation
 * type.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
@Aspect
public class DBOperationDescHandler {

    private HandlerProcessor processor = new HandlerProcessor();

    /**
     * Proceed.
     *
     * @param joinPoint
     * @return
     * @throws Throwable Object
     */
    public Object adviceDbOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint
                .getSignature();
        Method targetMethod = methodSignature.getMethod();
        DBOperationDesc annotation = targetMethod
                .getAnnotation(DBOperationDesc.class);
        if (null != annotation) {
            return process(joinPoint, annotation);
        }

        Class[] params = targetMethod.getParameterTypes();
        if (0 == params.length) {
            return joinPoint.proceed();
        } else {
            annotation = new DBOperationDesc() {
                @Override
                public String db() {
                    return "";
                }

                @Override
                public Class<? extends Annotation> annotationType() {
                    return DBOperationDesc.class;
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
                             DBOperationDesc annotation) throws Throwable {
        processor.clearContextRw();
        String sqlType = annotation.db();
        processor.setRwType(sqlType);
        Object result = joinPoint.proceed();
        processor.clearContextRw();
        return result;
    }

}
