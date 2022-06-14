package com.marketGola.User.controller;

import com.marketGola.User.domain.User;
import com.marketGola.User.request.UserDto;
import com.marketGola.User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 우선 회원 가입 (/signup) 과 회원 찾기 (/{userId}) 기능 추가.
 */

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public String signUp(@RequestBody UserDto userDto) {
        userService.save(userDto);
        return "가입이 완료되었습니다";
    }

    @GetMapping("/{userId}")
    public User user(@PathVariable long userId) {
        return userService.findById(userId).orElseThrow(() -> new NullPointerException("없는 사용자입니다"));
    }
}


