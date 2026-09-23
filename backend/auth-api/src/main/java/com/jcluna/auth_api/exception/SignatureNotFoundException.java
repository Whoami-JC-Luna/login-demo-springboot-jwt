package com.jcluna.auth_api.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SignatureNotFoundException extends RuntimeException {
  public SignatureNotFoundException(String message) {
    super(message);
  }
}
