package com.flab.marketgola.user.argumentresolver;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static com.flab.marketgola.common.constant.SessionConstant.NOT_CREATE_WHEN_NOT_EXIST;

import com.flab.marketgola.user.domain.LoginUser;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(AuthenticatedUser.class);
        boolean isParameterMatch = LoginUser.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && isParameterMatch;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        return request.getSession(NOT_CREATE_WHEN_NOT_EXIST).getAttribute(LOGIN_KEY);
    }
}
