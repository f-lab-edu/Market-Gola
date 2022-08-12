package com.flab.marketgola.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationGlobalError {

    private String errorCode;
    private String message;
}
