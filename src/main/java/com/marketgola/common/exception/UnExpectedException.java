package com.marketgola.common.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * 예상치 못한 예외는 Internal Server Error로 반환.
 */
public class UnExpectedException extends BaseException {

    private static final String message = "예상치 못한 예외입니다";

    public UnExpectedException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, message, cause);
    }

}
