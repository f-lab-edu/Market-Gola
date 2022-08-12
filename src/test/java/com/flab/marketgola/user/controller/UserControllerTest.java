package com.flab.marketgola.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.dto.UserJoinDto;
import com.flab.marketgola.user.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@ActiveProfiles("unit")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("모든 정보가 다 있고 규격에 맞는 경우 가입에 성공한다.")
    @Test
    void join() throws Exception {
        //given
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(ValidUser.GENDER)
                .birth(ValidUser.BIRTH)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(userJoinDto);

        //then
        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @DisplayName("패스워드를 입력하지 않았다면 가입에 실패한다.")
    @Test
    void join_no_password_fail() throws Exception {
        //given
        UserJoinDto noPasswordUser = UserJoinDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(noPasswordUser);

        //then
        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].field").value("password"))
                .andExpect(jsonPath("$.validationErrors[0].rejectedValue").doesNotExist())
                .andDo(print());
    }

    @DisplayName("주소를 입력하지 않았다면 가입에 실패한다.")
    @Test
    void join_no_address_fail() throws Exception {
        //given
        UserJoinDto noAddressUser = UserJoinDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .build();

        String content = objectMapper.writeValueAsString(noAddressUser);

        //then
        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].field").value("address"))
                .andExpect(jsonPath("$.validationErrors[0].rejectedValue").doesNotExist())
                .andDo(print());
    }

    @DisplayName("성별 타입에 맞지 않는 값이 인풋으로 올 경우 가입에 실패한다.")
    @Test
    void join_gender_strange_value_fail() throws Exception {
        //given
        Map<String, String> map = new HashMap<>();
        map.put("gender", "hello world");

        String content = objectMapper.writeValueAsString(map);

        //then
        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationError.field").value("gender"))
                .andDo(print());
    }

    @DisplayName("로그인 id가 형식에 어긋날 경우 가입에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"sgo", "한글아이디", "abc123!"})
    void join_id_wrong_form(String loginId) throws Exception {
        //given
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .loginId(loginId)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(userJoinDto);

        //then
        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].field").value("loginId"))
                .andDo(print());
    }

    @DisplayName("패스워드가 형식에 어긋날 경우 가입에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"short123", "onlyalphabet", "12345678910", "blank 123123!"})
    void join_pw_wrong_form(String password) throws Exception {
        //given
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(password)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(userJoinDto);

        //then
        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].field").value("password"))
                .andDo(print());
    }

    @DisplayName("이메일이 형식에 어긋날 경우 가입에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"justalpha", "abc123@@google.com"})
    void join_email_wrong_form(String email) throws Exception {
        //given
        UserJoinDto userJoinDto = UserJoinDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(email)
                .name(ValidUser.NAME)
                .password("abc123123123!")
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(userJoinDto);

        //then
        mockMvc.perform(post("/users")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors[0].field").value("email"))
                .andDo(print());
    }

    @DisplayName("정상적인 id 중복 확인 요청에 대해서 HTTPStatus OK를 반환한다.")
    @Test
    void checkIdDuplication() throws Exception {
        //given
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        List<String> values = new ArrayList<>();
        values.add(ValidUser.LOGIN_ID);
        map.put("loginId", values);

        //then
        mockMvc.perform(get("/users/id-exists")
                        .params(map)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}