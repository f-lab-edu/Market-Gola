package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class ApiError {

    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String message;
    private final String path;
    /**
     * Validation Error와 같은 구체적인 sub error를 담는 리스트
     */
    private List<ApiSubError> subErrors;

    public ApiError(HttpStatus status, String message, String path) {
        timestamp = LocalDateTime.now();
        subErrors = new ArrayList<>();
        this.status = status;
        this.message = message;
        this.path = path;
    }

    public void addApiSubError(ApiSubError apiSubError) {
        subErrors.add(apiSubError);
    }
}
