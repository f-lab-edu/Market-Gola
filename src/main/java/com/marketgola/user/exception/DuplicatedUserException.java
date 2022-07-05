package com.marketgola.user.exception;

import com.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * 원인이 파악된 에러이기 때문에 DEBUG 로 로깅 설정.
 */
public class DuplicatedUserException extends BaseException {

    public DuplicatedUserException(String message) {
        super(message, HttpStatus.BAD_REQUEST, LogLevel.DEBUG);
    }
}
