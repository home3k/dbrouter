/*
 * Copyright 2014 51haoyayi.com Inc Limited.
 * All rights reserved.
 */

package com.haoyayi.dbrouter.core.exception;

/**
 * Common exception in dbrouter module.
 *
 * @author home3k (sunkai@51haoyayi.com)
 */
public class DbRouterException extends RuntimeException {

    private static final long serialVersionUID = 4865813146714936866L;

    public DbRouterException() {
        super();
    }

    public DbRouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbRouterException(String message) {
        super(message);
    }

    public DbRouterException(Throwable cause) {
        super(cause);
    }

    public String getMessage() {
        return buildMessage(super.getMessage(), getCause());
    }

    public boolean contains(Class<?> exType) {
        if (exType == null) {
            return false;
        }
        if (exType.isInstance(this)) {
            return true;
        }
        Throwable cause = getCause();
        if (cause == this) {
            return false;
        }
        while (cause != null) {
            if (exType.isInstance(cause)) {
                return true;
            }
            if (cause.getCause() == cause) {
                break;
            }
            cause = cause.getCause();
        }
        return false;
    }

    protected static String buildMessage(String message, Throwable cause) {
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }
            sb.append("Nested exception is ").append(cause);
            return sb.toString();
        } else {
            return message;
        }
    }

}
