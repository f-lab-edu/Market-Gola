package com.flab.marketgola.user.controller;

import com.flab.marketgola.user.dto.request.CreateUserRequestDto;
import com.flab.marketgola.user.dto.request.GetUserRequestDto;
import com.flab.marketgola.user.dto.response.UserResponseDto;
import com.flab.marketgola.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(UserController.BASE_PATH)
@RestController
public class UserController {


    public static final String BASE_PATH = "/users";
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Validated @RequestBody CreateUserRequestDto createUserRequestDto) {
        return new ResponseEntity<>(userService.create(createUserRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(
            @ModelAttribute GetUserRequestDto getUserRequestDto) {
        return new ResponseEntity<>(userService.getByCondition(getUserRequestDto), HttpStatus.OK);
    }

}