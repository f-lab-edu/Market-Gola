package com.marketgola.user.exception;

import com.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * 원인이 명료하지 않기 때문에 ERROR 로 로깅 설정.
 */

public class BadRequestException extends BaseException {

    public BadRequestException(HttpStatus status, LogLevel logLevel, String message) {
        super(HttpStatus.BAD_REQUEST, LogLevel.ERROR, message);
    }

}
