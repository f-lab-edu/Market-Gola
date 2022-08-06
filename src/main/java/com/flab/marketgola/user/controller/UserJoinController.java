package com.flab.marketgola.user.controller;

import com.flab.marketgola.user.dto.UserJoinDto;
import com.flab.marketgola.user.service.UserService;
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
@RequestMapping("users")
@RestController
public class UserJoinController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public void join(@Validated @RequestBody UserJoinDto userJoinDto) throws Exception {
        userService.join(userJoinDto);
    }

    @GetMapping("/id-exists")
    public boolean checkIdDupllication(@RequestParam String loginId) {
        return userService.isDuplicatedLoginId(loginId);
    }

    @GetMapping("/email-exists")
    public boolean checkEmailDupllication(@RequestParam String email) {
        return userService.isDuplicatedEmail(email);
    }

    @GetMapping("/phone-number-exists")
    public boolean checkPhoneNumberDupllication(@RequestParam String phoneNumber) {
        return userService.isDuplicatedPhoneNumber(phoneNumber);
    }
}