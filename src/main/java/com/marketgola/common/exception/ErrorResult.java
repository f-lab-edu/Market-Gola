package com.marketgola.common.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 사용자에게 전달 될 에러 응답 형식 지정 spring boot에서 기본적으로 제공해주는 Basic Error controller - DefaultErrorAttributes을
 * 참고하여 변수 지정
 */
@Data
@AllArgsConstructor
@Builder
public class ErrorResult {

    private final LocalDateTime timestamp = LocalDateTime.now(); //"2019-02-15T21:48:44.447+0000"
    private final int status; //ex. 404
    private final String error; //ex. "Not Found",
    private final String message; //ex. "Data Not Found"
    private final String path; //"/123"

}
