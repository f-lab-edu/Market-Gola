package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class SampleFatalException extends BaseException {

    public SampleFatalException(HttpStatus responseHttpStatus, LogLevel logLevel) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, logLevel);
    }


    public SampleFatalException(HttpStatus responseHttpStatus, LogLevel logLevel, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, logLevel, message);
    }

}
