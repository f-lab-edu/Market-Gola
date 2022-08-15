package com.flab.marketgola.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationGlobalError extends ApiSubError {

    private String errorCode;
}
