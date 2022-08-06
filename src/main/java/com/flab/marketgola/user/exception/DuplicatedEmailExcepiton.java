package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class DuplicatedEmailExcepiton extends BaseException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public DuplicatedEmailExcepiton(LogLevel logLevel) {
        super(HttpStatus.CONFLICT, logLevel, MESSAGE);
    }
}
