package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class DuplicatedEmailExcepiton extends BaseException {

    public DuplicatedEmailExcepiton() {
        super(HttpStatus.CONFLICT, LogLevel.DEBUG);
    }
}
