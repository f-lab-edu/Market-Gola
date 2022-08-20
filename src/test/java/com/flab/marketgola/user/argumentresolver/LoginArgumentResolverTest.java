package com.flab.marketgola.user.argumentresolver;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;

class LoginArgumentResolverTest {

    void testMethod(@AuthenticatedUser String notLoginUser) {
    }

    @DisplayName("LoginUser 클래스가 아니라면 지원하지 않는다.")
    @Test
    void supportsParameter_not_LoginUser_fail()
            throws NoSuchMethodException {
        //given
        LoginArgumentResolver loginArgumentResolver = new LoginArgumentResolver();
        //when
        Method testMethod = LoginArgumentResolverTest.class.getDeclaredMethod("testMethod",
                String.class);
        //then
        MethodParameter methodParameter = new MethodParameter(testMethod, 0);
        assertFalse(loginArgumentResolver.supportsParameter(methodParameter));
    }
}