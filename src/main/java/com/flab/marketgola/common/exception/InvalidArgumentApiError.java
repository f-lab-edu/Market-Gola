package com.flab.marketgola.common.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

@Getter
@Setter
public class InvalidArgumentApiError extends ApiError<List<ApiSubError>> {

    private List<ApiSubError> detail = new ArrayList();

    public InvalidArgumentApiError(HttpStatus httpStatus, MethodArgumentNotValidException ex,
            WebRequest request) {
        super(httpStatus, ex, request);

        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> detail.add(
                        new ValidationFieldError(
                                fieldError.getField(),
                                fieldError.getRejectedValue(),
                                fieldError.getDefaultMessage()))
                );
    }
}
