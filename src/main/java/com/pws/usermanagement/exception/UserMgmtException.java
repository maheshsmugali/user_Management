package com.pws.usermanagement.exception;

import org.springframework.http.HttpStatus;

public class UserMgmtException extends RuntimeException {

    final HttpStatus httpStatus;

    public UserMgmtException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
