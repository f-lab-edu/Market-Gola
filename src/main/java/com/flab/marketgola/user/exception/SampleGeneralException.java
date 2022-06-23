package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public class SampleGeneralException extends BaseException {

    public SampleGeneralException(HttpStatus responseHttpStatus, LogLevel logLevel) {
        super(HttpStatus.BAD_REQUEST, logLevel);
    }


    public SampleGeneralException(HttpStatus responseHttpStatus, LogLevel logLevel,
            String message) {
        super(HttpStatus.BAD_REQUEST, logLevel, message);
    }
}
