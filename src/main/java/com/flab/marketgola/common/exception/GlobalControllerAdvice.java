package com.flab.marketgola.common.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 공통적인 API 예외 처리를 모아 놓는 클래스
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    /**
     * 직접 정의한 예외(BaseException을 상속하는 예외)가 아닌 예상치 못한 예외를 처리하는 핸들러
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiError unExpectedExceptionHandler(Exception e,
            HttpServletRequest request) {
        doLog(LogLevel.ERROR, e, request);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 예외입니다.",
                request.getRequestURI());
    }

    /**
     * 예외 처리 핸들러 BaseException 하위의 예외를 받아서 예외 결과를 일관되게 반환한다.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError> baseExceptionHandler(BaseException e,
            HttpServletRequest request) {
        doLog(e.getLogLevel(), e, request);
        ApiError apiError = new ApiError(e.getResponseHttpStatus(), e.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(apiError, e.getResponseHttpStatus());
    }

    /**
     * 잘못된 인풋 타입으로 인해 Json deserialization이 실패했을 때 처리하는 핸들러
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiError inputTypeMismatchException(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        doLog(LogLevel.DEBUG, e, request);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다.",
                request.getRequestURI());
        if (e.getCause() instanceof JsonMappingException) {
            apiError.addApiSubError(new ValidationFieldError((JsonMappingException) e.getCause()));
        }
        return apiError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError invalidFieldExceptionHandler(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        doLog(LogLevel.DEBUG, e, request);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다.",
                request.getRequestURI());

        e.getBindingResult().getFieldErrors()
                .forEach(fieldError -> apiError.addApiSubError(
                        new ValidationFieldError(fieldError)));
        return apiError;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiError missingParameterExceptionHandler(MissingServletRequestParameterException e,
            HttpServletRequest request) {
        doLog(LogLevel.DEBUG, e, request);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "필요한 파라미터를 전달하지 않았습니다.",
                request.getRequestURI());
        apiError.addApiSubError(
                new MissingParameterError(e.getParameterName(), e.getParameterType()));
        return apiError;
    }

    private void doLog(LogLevel logLevel, Exception e, HttpServletRequest request) {
        String logMessage = String.format("요청 uri = %s, http method = %s", request.getRequestURI(),
                request.getMethod());

        if (logLevel == LogLevel.ERROR) {
            log.error(logMessage, e);
        } else if (logLevel == LogLevel.WARN) {
            log.warn(logMessage, e);
        } else if (logLevel == LogLevel.INFO) {
            log.info(logMessage, e);
        } else {
            log.debug(logMessage, e);
        }
    }
}