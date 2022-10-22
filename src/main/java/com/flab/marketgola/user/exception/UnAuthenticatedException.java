package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class UnAuthenticatedException extends BaseException {

    public UnAuthenticatedException() {
        super(HttpStatus.UNAUTHORIZED, LogLevel.DEBUG, "로그인하셔야 본 서비스를 이용할 수 있습니다.");
    }
}
