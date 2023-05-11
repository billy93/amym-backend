package com.atibusinessgroup.amanyaman.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.atibusinessgroup.amanyaman.security.UserLockedException;
import com.atibusinessgroup.amanyaman.web.rest.errors.UnlockedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, LockedException.class, UserLockedException.class, UnlockedException.class})
    public ResponseEntity<String> handleException(Exception exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = exception.getMessage();

        return ResponseEntity.status(status).body(message);
    }

}