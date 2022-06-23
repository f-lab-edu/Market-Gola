package com.flab.marketgola.common.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

class UnExpectedException extends BaseException {

    private static final String ERROR_MESSAGE = "예상치 못한 예외가 발생했습니다.";

    public UnExpectedException(Exception cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, ERROR_MESSAGE, cause);

    }
}
