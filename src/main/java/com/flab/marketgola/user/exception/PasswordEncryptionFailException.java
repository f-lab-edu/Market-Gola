package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class PasswordEncryptionFailException extends BaseException {

    public static final String MESSAGE = "패스워드를 암호화하는데 실패했습니다.";

    public PasswordEncryptionFailException(Exception cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR, MESSAGE, cause);
    }
}
