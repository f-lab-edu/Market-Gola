package com.flab.marketgola.user.controller;

import com.flab.marketgola.user.argumentresolver.AuthenticatedUser;
import com.flab.marketgola.user.domain.LoginUser;
import com.flab.marketgola.user.dto.request.CreateUserRequestDto;
import com.flab.marketgola.user.dto.request.GetUserRequestDto;
import com.flab.marketgola.user.dto.response.UserPrivateInfoResponseDto;
import com.flab.marketgola.user.dto.response.UserPublicInfoResponseDto;
import com.flab.marketgola.user.service.UserService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    public static final String GET_MY_INFO_PATH = "/me";
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserPrivateInfoResponseDto> createUser(
            @Validated @RequestBody CreateUserRequestDto createUserRequestDto)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new ResponseEntity<>(userService.createUser(createUserRequestDto),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserPublicInfoResponseDto> getUser(
            @ModelAttribute GetUserRequestDto getUserRequestDto) {
        return new ResponseEntity<>(userService.getUser(getUserRequestDto), HttpStatus.OK);
    }

    @GetMapping(GET_MY_INFO_PATH)
    public ResponseEntity<UserPrivateInfoResponseDto> getMyInfo(
            @AuthenticatedUser LoginUser loginUser,
            @ModelAttribute GetUserRequestDto getUserRequestDto) {
        return new ResponseEntity<>(userService.getMyInfo(loginUser.getId()), HttpStatus.OK);
    }
}