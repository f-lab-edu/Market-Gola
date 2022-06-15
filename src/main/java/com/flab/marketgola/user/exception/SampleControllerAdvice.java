package com.flab.marketgola.user.exception;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.flab.marketgola.user") // 예외 처리 범위 등록
public class SampleControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 상태코드 등록
    @ExceptionHandler({NullPointerException.class}) // 이 핸들러로 처리하고자 하는 예외 등록
    public SampleErrorResult exHandle(Exception e, HttpServletRequest request, ZoneId zoneId) {
        log.error("예외 관련 메세지", e); //예외에 대한 로그

        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(LocalDateTime.now(zoneId));
        int status = HttpStatus.BAD_REQUEST.value();
        String detail = HttpStatus.BAD_REQUEST.getReasonPhrase();
        String path = request.getRequestURI();
        return new SampleErrorResult(timestamp, status, detail, path);
    }
}
