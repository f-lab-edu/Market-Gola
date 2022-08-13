package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class NoSuchUserException extends BaseException {

    public NoSuchUserException() {
        super(HttpStatus.NOT_FOUND, LogLevel.DEBUG);
    }

}
