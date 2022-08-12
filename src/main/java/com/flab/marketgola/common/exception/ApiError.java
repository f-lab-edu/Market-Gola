package com.flab.marketgola.common.exception;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Getter
@Setter
public class ApiError {

    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String errorCode;
    private final String path;

    public ApiError(HttpStatus status, Exception exception, WebRequest request) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.errorCode = exception.getClass().getSimpleName();
        this.path = ((ServletWebRequest) request).getRequest().getRequestURI();
    }

}
