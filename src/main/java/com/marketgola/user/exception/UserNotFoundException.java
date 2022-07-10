package com.marketgola.user.exception;

import com.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * 기본값으로 Debug 에러레벨 지정. 필요에 따라 logLevel 수준을 설정할 수 있도록 생성자 추가.
 */

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, LogLevel.DEBUG, message);
    }

    public UserNotFoundException(LogLevel logLevel, String message) {
        super(HttpStatus.NOT_FOUND, logLevel, message);
    }

}
