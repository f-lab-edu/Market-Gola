package com.flab.marketgola.common.exception;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ErrorResult {

    /**
     * 예외 발생 시각
     */
    private final LocalDateTime timestamp;
    /**
     * 반환할 HTTP 상태코드
     */
    private final int status;
    /**
     * HTTP 상태코드 관련 메세지 ex. Not Found
     */
    private final String error;
    /**
     * 어떤 에러인지 ex. UserNotFoundException
     */
    private final String type;
    /**
     * 에러에 대한 설명 ex. can't find user
     */
    private final String message;
}
