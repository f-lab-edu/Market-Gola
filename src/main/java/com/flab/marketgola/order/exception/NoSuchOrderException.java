package com.flab.marketgola.order.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class NoSuchOrderException extends BaseException {

    public NoSuchOrderException() {
        super(HttpStatus.NOT_FOUND, LogLevel.DEBUG);
    }
}
