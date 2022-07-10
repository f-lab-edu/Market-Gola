package com.marketgola.common.exception;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler 는 Common 에서 공통적으로 정의한다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {


    /**
     * 애플리케이션에서 지정한 에러(base Exception)을 처리하는 핸들러
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResult> baseExceptionHandler(BaseException e,
            HttpServletRequest request) {
        createLog(e, request);
        return createErrorResult(e, request);
    }

    /**
     * 예상치 못한 (base Exception 을 구현하지 않는 Exception)을 처리하는 핸들러
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> unExpectedHandler(Exception e, HttpServletRequest request) {
        UnExpectedException unexpectedException = new UnExpectedException(e);
        createLog(unexpectedException, request);
        return createErrorResult(unexpectedException, request);
    }

    /**
     * BaseException/UnexpectedException 을 모두 ErrorResult 형태로 반환
     */
    public ResponseEntity<ErrorResult> createErrorResult(BaseException e,
            HttpServletRequest request) {
        return ResponseEntity.status(e.getStatus())
                .body(ErrorResult.builder().status(e.getStatus().value())
                        .error(e.getStatus().name())
                        .message(e.getMessage())
                        .path(request.getRequestURI())
                        .build());
    }

    /**
     * 예외 레벨 별 로그처리
     */
    public void createLog(BaseException e, HttpServletRequest request) {
        LogLevel level = e.getLogLevel();
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        String logFormat = String.format("status: (%d) method: (%s) path: (%s)",
                e.getStatus().value(),
                method,
                requestURI);

        if (level == LogLevel.ERROR) {
            log.error(logFormat, e);
        } else if (level == LogLevel.WARN) {
            log.warn(logFormat, e);
        } else if (level == LogLevel.INFO) {
            log.info(logFormat, e);
        } else if (level == LogLevel.DEBUG) {
            log.debug(logFormat, e);
        }
    }
}

