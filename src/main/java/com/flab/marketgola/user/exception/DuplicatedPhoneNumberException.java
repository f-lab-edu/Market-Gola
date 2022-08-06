package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class DuplicatedPhoneNumberException extends BaseException {

    private static final String MESSAGE = "이미 존재하는 핸드폰 번호입니다.";

    public DuplicatedPhoneNumberException(LogLevel logLevel) {
        super(HttpStatus.CONFLICT, logLevel, MESSAGE);
    }
}
