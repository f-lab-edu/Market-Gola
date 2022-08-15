package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class ApiError<T> {

    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String errorCode;
    private final String path;
    private T detail;

    public ApiError(HttpStatus status, Exception exception, WebRequest request) {
        timestamp = LocalDateTime.now();
        this.status = status;
        this.errorCode = parseErrorCode(exception);
        this.path = ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    private String parseErrorCode(Exception exception) {
        String className = exception.getClass().getSimpleName();
        return className.substring(0, className.length() - "Exception".length());
    }

}
