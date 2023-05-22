package com.atibusinessgroup.amanyaman.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class, LockedException.class})
    public ResponseEntity<Response> handleException(Exception exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = exception.getMessage();

        Response resp = new Response();
        resp.setMessage(message);
        return ResponseEntity.status(status).body(resp);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleSExpiredJwtException(SignatureException e) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT signature does not match", e);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void handleSignatureException(SignatureException e) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT signature does not match", e);
    }

    public class Response{
        public String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}