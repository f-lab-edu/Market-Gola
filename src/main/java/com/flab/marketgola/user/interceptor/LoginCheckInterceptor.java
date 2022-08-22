package com.flab.marketgola.user.interceptor;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static com.flab.marketgola.common.constant.SessionConstant.NOT_CREATE_WHEN_NOT_EXIST;

import com.flab.marketgola.user.exception.UnauthorizedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {

        HttpSession session = request.getSession(NOT_CREATE_WHEN_NOT_EXIST);

        if (isLogin(session)) {
            return true;
        }

        throw new UnauthorizedException();
    }

    private boolean isLogin(HttpSession session) {
        return session != null && session.getAttribute(LOGIN_KEY) != null;
    }

}
