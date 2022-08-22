package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 공통적인 API 예외 처리를 모아 놓는 클래스
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * 직접 정의한 예외(BaseException을 상속하는 예외)가 아닌 예상치 못한 예외를 처리하는 핸들러
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnExpectedException(Exception e, WebRequest request) {
        return handleExceptionInternal(new UnExpectedException(e), null, new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    /**
     * 예외 처리 핸들러 BaseException 하위의 예외를 받아서 예외 결과를 일관되게 반환한다.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleBaseException(BaseException e, WebRequest request) {
        return handleExceptionInternal(e, null, new HttpHeaders(), e.getResponseHttpStatus(),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        if (ex.getCause() instanceof InvalidFormatException) {
            return handleInvalidFormat((InvalidFormatException) ex.getCause(), headers, status,
                    request);
        }

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    /**
     * 잘못된 인풋 타입으로 인해 Json deserialization이 실패했을 때 처리하는 핸들러
     */
    public ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        InvalidFormatApiError apiError = new InvalidFormatApiError(status, ex, request);

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        InvalidArgumentApiError apiError = new InvalidArgumentApiError(status, ex, request);

        return handleExceptionInternal(ex, apiError, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (body == null) {
            body = new ApiError(status, ex, request);
        }

        doLog(ex, (ServletWebRequest) request);

        return new ResponseEntity<>(body, headers, status);
    }

    private void doLog(Exception e, ServletWebRequest request) {
        String logMessage = String.format("요청 uri = %s, http method = %s",
                request.getRequest().getRequestURI(), request.getHttpMethod());

        LogLevel logLevel = getLogLevel(e);

        if (logLevel == LogLevel.ERROR) {
            log.error(logMessage, e);
        } else if (logLevel == LogLevel.WARN) {
            log.warn(logMessage, e);
        } else if (logLevel == LogLevel.INFO) {
            log.info(logMessage, e);
        } else if (logLevel == LogLevel.DEBUG) {
            log.debug(logMessage, e);
        }
    }

    private LogLevel getLogLevel(Exception e) {
        LogLevel logLevel = LogLevel.DEBUG;

        if (e instanceof BaseException) {
            logLevel = ((BaseException) e).getLogLevel();
        } else if (e instanceof UnExpectedException) {
            logLevel = LogLevel.ERROR;
        }

        return logLevel;
    }
}