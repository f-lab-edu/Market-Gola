package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class PasswordValidationFailException extends BaseException {

    public static final String MESSAGE = "패스워드를 검증하는데 실패했습니다.";

    public PasswordValidationFailException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, MESSAGE, cause);
    }
}
