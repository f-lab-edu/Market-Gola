package com.flab.marketgola.product.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class NoSuchCategoryException extends BaseException {

    public NoSuchCategoryException(Exception cause) {
        super(HttpStatus.NOT_FOUND, LogLevel.DEBUG, cause);
    }
}
