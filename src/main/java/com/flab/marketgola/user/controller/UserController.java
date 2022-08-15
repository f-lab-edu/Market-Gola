package com.flab.marketgola.user.controller;

import com.flab.marketgola.user.dto.UserJoinDto;
import com.flab.marketgola.user.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(UserController.BASE_PATH)
@RestController
public class UserController {

    public static final String BASE_PATH = "/users";
    private final JoinService joinService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void join(@Validated @RequestBody UserJoinDto userJoinDto) {
        joinService.join(userJoinDto);
    }

    @GetMapping("/id-exists")
    public boolean checkIdDupllication(@RequestParam String loginId) {
        return joinService.isDuplicatedLoginId(loginId);
    }

    @GetMapping("/email-exists")
    public boolean checkEmailDupllication(@RequestParam String email) {
        return joinService.isDuplicatedEmail(email);
    }

    @GetMapping("/phone-number-exists")
    public boolean checkPhoneNumberDupllication(@RequestParam String phoneNumber) {
        return joinService.isDuplicatedPhoneNumber(phoneNumber);
    }
}