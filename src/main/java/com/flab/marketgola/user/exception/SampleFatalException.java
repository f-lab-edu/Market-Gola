package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.FatalException;
import org.springframework.http.HttpStatus;

public class SampleFatalException extends FatalException {

    public SampleFatalException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public SampleFatalException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}
