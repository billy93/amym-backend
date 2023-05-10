package com.atibusinessgroup.amanyaman.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of a not activated user trying to authenticate.
 */
public class UserLockedException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public UserLockedException(String message) {
        super(message);
    }

    public UserLockedException(String message, Throwable t) {
        super(message, t);
    }
}
