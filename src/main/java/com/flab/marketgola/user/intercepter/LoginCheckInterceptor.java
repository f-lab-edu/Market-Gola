package com.flab.marketgola.user.intercepter;

import static com.flab.marketgola.common.controller.SessionConst.LOGIN_KEY;
import static com.flab.marketgola.common.controller.SessionConst.NOT_CREATE_WHEN_NOT_EXIST;

import com.flab.marketgola.user.controller.UserController;
import com.flab.marketgola.user.exception.UnauthorizedException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {

        HttpSession session = request.getSession(NOT_CREATE_WHEN_NOT_EXIST);

        if (isLogin(session) || isJoinPath(request)) {
            return true;
        }

        throw new UnauthorizedException();
    }

    private boolean isLogin(HttpSession session) {
        return session != null && session.getAttribute(LOGIN_KEY) != null;
    }

    private boolean isJoinPath(HttpServletRequest request) {
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        return HttpMethod.POST.matches(httpMethod) && requestURI.equals(UserController.BASE_PATH);
    }
}
