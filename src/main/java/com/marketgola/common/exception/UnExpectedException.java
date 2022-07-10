package com.marketgola.common.exception;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * 예상치 못한 예외는 Internal Server Error로 반환. 어떤 예외인지 파악하기 위해 Throwable cause 를 인자로 생성한다.
 */
public class UnExpectedException extends BaseException {

    private static final String message = "예상치 못한 예외입니다";

    public UnExpectedException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.DEBUG, message, cause);

    }
}
