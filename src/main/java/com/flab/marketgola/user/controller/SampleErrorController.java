package com.flab.marketgola.user.controller;

import com.flab.marketgola.user.exception.SampleFatalException;
import com.flab.marketgola.user.exception.SampleGeneralException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleErrorController {

    @GetMapping("/general")
    public String nullException() {
        throw new SampleGeneralException(HttpStatus.BAD_REQUEST, LogLevel.DEBUG, "일반적인 예외입니다.");
    }

    @GetMapping("/fatal")
    public String fatalException() {
        throw new SampleFatalException(HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR,
            "심각한 예외입니다.");
    }

    @GetMapping("/unExpected")
    public String unExpectedException() {
        throw new IllegalCallerException();
    }
}
