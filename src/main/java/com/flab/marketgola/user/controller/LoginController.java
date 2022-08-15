package com.flab.marketgola.user.controller;

import static com.flab.marketgola.common.controller.SessionConst.CREATE_WHEN_NOT_EXIST;
import static com.flab.marketgola.common.controller.SessionConst.LOGIN_KEY;

import com.flab.marketgola.user.dto.UserLoginDto;
import com.flab.marketgola.user.service.LoginService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    public static final String LOGIN_PATH = "/login";
    public static final String LOGOUT_PATH = "/logout";
    private final LoginService loginService;

    @PostMapping(LOGIN_PATH)
    public void login(@Validated @RequestBody UserLoginDto userLoginDto,
            HttpServletRequest request) {
        String name = loginService.login(userLoginDto);
        request.getSession(CREATE_WHEN_NOT_EXIST).setAttribute(LOGIN_KEY, name);
    }

    @PostMapping(LOGOUT_PATH)
    public void logout(HttpServletRequest request) {
        request.getSession(CREATE_WHEN_NOT_EXIST).invalidate();
    }

}

