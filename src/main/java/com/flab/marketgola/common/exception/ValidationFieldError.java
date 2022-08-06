package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.FieldError;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class ValidationFieldError implements ApiSubError {

    String field;
    Object rejectedValue;
    String message;

    public ValidationFieldError(JsonMappingException jsonMappingException) {
        this.field = jsonMappingException.getPath().get(0).getFieldName();
        this.message = "잘못된 타입의 입력값입니다.";
    }

    public ValidationFieldError(FieldError error) {
        this.field = error.getField();
        this.rejectedValue = error.getRejectedValue();
        this.message = error.getDefaultMessage();
    }
}
