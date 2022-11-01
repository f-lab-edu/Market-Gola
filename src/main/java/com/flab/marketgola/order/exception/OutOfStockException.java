package com.flab.marketgola.order.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class OutOfStockException extends BaseException {

    public OutOfStockException() {
        super(HttpStatus.CONFLICT, LogLevel.DEBUG);
    }
}
