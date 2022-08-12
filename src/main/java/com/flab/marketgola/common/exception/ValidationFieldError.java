package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class ValidationFieldError {

    private String field;
    private Object rejectedValue;
    private String message;

    public ValidationFieldError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public ValidationFieldError(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
