package com.marketgola.common.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * BaseException: Exception 의 공통 기능 정의 직접 정의하는 Exception은 모두 Base Exception 을 상속 받는다.
 */

@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final LogLevel logLevel;

    public BaseException(HttpStatus status, LogLevel logLevel, String message) {
        super(message);
        this.status = status;
        this.logLevel = logLevel;
    }

    public BaseException(HttpStatus status, LogLevel logLevel, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.logLevel = logLevel;
    }
    
}
