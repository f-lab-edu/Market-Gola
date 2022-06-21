package com.flab.marketgola.common.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {

    /**
     * 예외처리시 필요한 http 반환 상태코드 모든 예외는 이 상태코드를 등록해줘야 한다.
     */
    private final HttpStatus responseHttpStatus;
    private final LogLevel logLevel;

    public BaseException(HttpStatus responseHttpStatus, LogLevel logLevel) {
        super();
        this.responseHttpStatus = responseHttpStatus;
        this.logLevel = logLevel;
    }

    public BaseException(HttpStatus responseHttpStatus, LogLevel logLevel, String message) {
        super(message);
        this.responseHttpStatus = responseHttpStatus;
        this.logLevel = logLevel;
    }

    public BaseException(HttpStatus responseHttpStatus, LogLevel logLevel, Exception cause) {
        super(cause);
        this.responseHttpStatus = responseHttpStatus;
        this.logLevel = logLevel;
    }

    public BaseException(HttpStatus responseHttpStatus, LogLevel logLevel,
        String message, Exception cause) {
        super(message, cause);
        this.responseHttpStatus = responseHttpStatus;
        this.logLevel = logLevel;
    }
}
