package com.flab.marketgola.user.controller;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.domain.LoginUser;
import com.flab.marketgola.user.dto.request.LoginRequestDto;
import com.flab.marketgola.user.service.LoginService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("unit")
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LoginService loginService;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("로그인에 성공하면 OK를 내려보내고 세션에 로그인한 유저 객체를 등록한다.")
    @Test
    void login() throws Exception {
        //given
        Mockito.when(loginService.login(any())).thenReturn(new LoginUser(1L, ValidUser.NAME));

        String content = objectMapper.writeValueAsString(
                new LoginRequestDto(ValidUser.LOGIN_ID, ValidUser.PASSWORD));

        MockHttpSession session = new MockHttpSession();

        //then
        mockMvc.perform(post(LoginController.LOGIN_PATH)
                        .session(session)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(session.getAttribute(LOGIN_KEY)).isNotNull();
    }

    @DisplayName("로그아웃에 성공하면 OK를 내려보내고 세션을 만료시킨다.")
    @Test
    void logout() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(LOGIN_KEY, new LoginUser(1L, ValidUser.NAME));

        //then
        mockMvc.perform(post(LoginController.LOGOUT_PATH)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(session.isInvalid()).isTrue();
    }
}
