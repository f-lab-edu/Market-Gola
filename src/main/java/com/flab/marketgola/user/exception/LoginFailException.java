package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class LoginFailException extends BaseException {

    private static final String MESSAGE = "아이디가 존재하지 않거나 비밀번호가 맞지 않습니다.";

    public LoginFailException() {
        super(HttpStatus.UNAUTHORIZED, LogLevel.DEBUG, MESSAGE);
    }

}
