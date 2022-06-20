package com.flab.marketgola.common.exception;

import org.springframework.http.HttpStatus;

public class FatalException extends BaseException {

    public FatalException(HttpStatus responseHttpStatus) {
        super(responseHttpStatus);
    }


    public FatalException(HttpStatus responseHttpStatus, String message) {
        super(responseHttpStatus, message);
    }
}
