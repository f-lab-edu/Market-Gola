package com.marketgola.common.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 사용자에게 전달 될 에러 응답 형식
 */
@Data
@AllArgsConstructor
@Builder
public class ErrorResult {

    private final LocalDateTime dateTime = LocalDateTime.now();
    private final int status; //ex. 404
    private final String error; //ex. "Not Found",
    private final String message; //ex. "Data Not Found"
    private final String url;

}
