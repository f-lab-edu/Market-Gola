package com.flab.marketgola.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleErrorController {

    @GetMapping("/null-exception")
    public String nullException() {
        throw new NullPointerException("널 값입니다.");
    }
}
