package com.flab.marketgola.user.controller;

import static com.flab.marketgola.common.constant.SessionConstant.CREATE_WHEN_NOT_EXIST;
import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;

import com.flab.marketgola.user.domain.LoginUser;
import com.flab.marketgola.user.dto.request.LoginRequestDto;
import com.flab.marketgola.user.service.LoginService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    public void login(@Validated @RequestBody LoginRequestDto loginRequestDto,
            HttpServletRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException {
        LoginUser loginUser = loginService.login(loginRequestDto);
        request.getSession(CREATE_WHEN_NOT_EXIST).setAttribute(LOGIN_KEY, loginUser);
    }

    @PostMapping(LOGOUT_PATH)
    public void logout(HttpServletRequest request) {
        request.getSession(CREATE_WHEN_NOT_EXIST).invalidate();
    }

}

