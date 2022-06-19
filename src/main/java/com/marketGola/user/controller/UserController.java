package com.marketGola.user.controller;

import com.marketGola.user.domain.User;
import com.marketGola.user.request.UserDto;
import com.marketGola.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 우선 회원 가입 (/signup) 과 회원 찾기 (/{userId}) 기능 추가.
 */

@RestController
@RequestMapping(value = "/users")
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
        return userService.findById(userId).orElseThrow(() -> new NullPointerException("USER NOT FOUND"));
    }
}


