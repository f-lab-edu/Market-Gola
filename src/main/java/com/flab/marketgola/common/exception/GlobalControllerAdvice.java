package com.flab.marketgola.common.exception;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 공통적인 API 예외 처리를 모아 놓는 클래스
 */
@Slf4j
@RestControllerAdvice()
public class GlobalControllerAdvice {


    /**
     * 직접 정의한 예외(BaseException을 상속하는 예외)가 아닌 예상치 못한 예외를 처리하는 핸들러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> unExpectedExceptionHandler(Exception e,
        HttpServletRequest request) {
        UnExpectedException unExpectedException = new UnExpectedException(e);
        doLog(unExpectedException, request);
        return createErrorResult(unExpectedException);
    }

    /**
     * 예외 처리 핸들러 BaseException 하위의 예외를 받아서 예외 결과를 일관되게 반환한다.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResult> baseExceptionHandler(BaseException e,
        HttpServletRequest request) {
        doLog(e, request);
        return createErrorResult(e);
    }


    /**
     * 예외 종류에 따라 다른 level로 로그를 찍는 메소드.
     */
    private void doLog(BaseException e, HttpServletRequest request) {
        String logMessage = "요청 uri = {}, http method = {}";

        if (e.getLogLevel() == LogLevel.ERROR) {
            log.error(logMessage, request.getRequestURI(), request.getMethod(), e);
        } else if (e.getLogLevel() == LogLevel.WARN) {
            log.warn(logMessage, request.getRequestURI(), request.getMethod(), e);
        } else if (e.getLogLevel() == LogLevel.INFO) {
            log.info(logMessage, request.getRequestURI(), request.getMethod(), e);
        } else {
            log.debug(logMessage, request.getRequestURI(), request.getMethod(), e);
        }

    }

    /**
     * 예외 결과를 만들어주는 메소드
     */
    private ResponseEntity<ErrorResult> createErrorResult(BaseException e) {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = e.getResponseHttpStatus().value();
        String error = e.getResponseHttpStatus().getReasonPhrase();
        String type = e.getClass().getSimpleName();
        String message = e.getMessage();
        ErrorResult errorResult = new ErrorResult(timestamp, status, error, type, message);
        return new ResponseEntity<>(errorResult, e.getResponseHttpStatus());
    }
}