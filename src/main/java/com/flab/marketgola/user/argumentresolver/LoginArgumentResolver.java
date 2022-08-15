package com.flab.marketgola.user.argumentresolver;

import static com.flab.marketgola.common.controller.SessionConst.LOGIN_KEY;
import static com.flab.marketgola.common.controller.SessionConst.NOT_CREATE_WHEN_NOT_EXIST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean isParameterMatch = String.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && isParameterMatch;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(NOT_CREATE_WHEN_NOT_EXIST);
        if (session == null) {
            return null;
        }

        return session.getAttribute(LOGIN_KEY);
    }
}
