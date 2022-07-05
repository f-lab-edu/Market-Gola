package com.marketgola.user.controller;

import com.marketgola.user.domain.User;
import com.marketgola.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signUp(@RequestBody User user) {
        userService.register(user);
    }


    @GetMapping("/dupCheck/{loginId}")
    public Boolean checkDuplicateLoginId(@PathVariable String loginId) {
        return userService.validateDuplicateMember(loginId);
    }

    @GetMapping("/{loginId}")
    public User findUser(@PathVariable String loginId) {
        return userService.findByIdOrElseThrowUserNotFound(loginId);
    }
}


