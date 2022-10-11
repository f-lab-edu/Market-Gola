package com.flab.marketgola.product.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class NoSuchProductException extends BaseException {

    public NoSuchProductException() {
        super(HttpStatus.NOT_FOUND, LogLevel.DEBUG);
    }
}
