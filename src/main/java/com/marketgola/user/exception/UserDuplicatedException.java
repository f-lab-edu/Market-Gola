package com.marketgola.user.exception;

import com.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * 기본값으로 Debug 에러레벨 지정. 필요에 따라 logLevel 수준을 설정할 수 있도록 생성자 추가.
 */

public class UserDuplicatedException extends BaseException {

    public UserDuplicatedException(String message) {
        super(HttpStatus.BAD_REQUEST, LogLevel.DEBUG, message);
    }

    public UserDuplicatedException(LogLevel logLevel, String message) {
        super(HttpStatus.BAD_REQUEST, logLevel, message);
    }
}
