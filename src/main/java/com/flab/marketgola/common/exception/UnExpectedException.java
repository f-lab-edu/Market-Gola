package com.flab.marketgola.common.exception;

public class UnExpectedException extends RuntimeException {

    public UnExpectedException(Exception cause) {
        super(cause);
    }
}
