package com.marketgola.user.controller;

import static com.marketgola.util.HttpResponses.RESPONSE_CONFLICT;
import static com.marketgola.util.HttpResponses.RESPONSE_OK;

import com.marketgola.user.domain.User;
import com.marketgola.user.service.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 우선 회원 가입 (/signup) 과 회원 찾기 (/{userId}) 기능 추가.
 */

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<Void> signUp(@RequestBody User user) {
        userService.register(user);
        return RESPONSE_OK;
    }

    @GetMapping("/dupCheck/{name}")
    public ResponseEntity<Void> checkDuplicateName(@PathVariable String name) {
        try {
            userService.validateDuplicateMember(name);
        } catch (IllegalStateException e) {
            return RESPONSE_CONFLICT;
        }
        return RESPONSE_OK;
    }

    @GetMapping("/{userId}")
    public Optional<User> findUser(@PathVariable Long userId) {
        return userService.findByIdOrElseNull(userId);
    }
}


