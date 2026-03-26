package com.jcluna.auth_api.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedQuoteAccessException extends RuntimeException {
    public UnauthorizedQuoteAccessException(String message) {
        super(message);
    }
}
