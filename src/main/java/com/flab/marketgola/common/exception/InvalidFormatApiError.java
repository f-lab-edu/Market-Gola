package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

@Getter
@Setter
public class InvalidFormatApiError extends ApiError {

    private ValidationFieldError validationError;

    public InvalidFormatApiError(HttpStatus httpStatus, InvalidFormatException exception,
            WebRequest request) {
        super(httpStatus, exception, request);

        String field = exception.getPath().get(0).getFieldName();
        String message = "잘못된 타입의 입력값입니다.";

        validationError = new ValidationFieldError(field, message);
    }
}
