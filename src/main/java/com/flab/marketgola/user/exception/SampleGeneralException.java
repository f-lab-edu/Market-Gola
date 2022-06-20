package com.flab.marketgola.user.exception;

import com.flab.marketgola.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class SampleGeneralException extends BaseException {

    public SampleGeneralException() {
        super(HttpStatus.BAD_REQUEST);
    }


    public SampleGeneralException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
