package com.flab.marketgola.user.controller;

import static com.flab.marketgola.common.constant.SessionConstant.LOGIN_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.domain.LoginUser;
import com.flab.marketgola.user.dto.request.CreateUserRequestDto;
import com.flab.marketgola.user.exception.NoSuchUserException;
import com.flab.marketgola.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    void createUser() throws Exception {
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(ValidUser.GENDER)
                .birth(ValidUser.BIRTH)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(createUserRequestDto);

        //then
        mockMvc.perform(post(UserController.BASE_PATH)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }


    @DisplayName("패스워드를 입력하지 않았다면 가입에 실패한다.")
    @Test
    void createUser_no_password_fail() throws Exception {
        //given
        CreateUserRequestDto noPasswordUser = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(noPasswordUser);

        //then
        mockMvc.perform(post(UserController.BASE_PATH)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail[0].field").value("password"))
                .andExpect(jsonPath("$.detail[0].rejectedValue").doesNotExist())
                .andDo(print());
    }

    @DisplayName("주소를 입력하지 않았다면 가입에 실패한다.")
    @Test
    void createUser_no_address_fail() throws Exception {
        //given
        CreateUserRequestDto noAddressUser = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .build();

        String content = objectMapper.writeValueAsString(noAddressUser);

        //then
        mockMvc.perform(post(UserController.BASE_PATH)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail[0].field").value("address"))
                .andExpect(jsonPath("$.detail[0].rejectedValue").doesNotExist())
                .andDo(print());
    }

    @DisplayName("성별 타입에 맞지 않는 값이 인풋으로 올 경우 가입에 실패한다.")
    @Test
    void createUser_gender_strange_value_fail() throws Exception {
        //given
        Map<String, String> map = new HashMap<>();
        map.put("gender", "hello world");

        String content = objectMapper.writeValueAsString(map);

        //then
        mockMvc.perform(post(UserController.BASE_PATH)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail.field").value("gender"))
                .andDo(print());
    }

    @DisplayName("로그인 id가 형식에 어긋날 경우 가입에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"sgo", "한글아이디", "abc123!"})
    void createUser_id_wrong_form(String loginId) throws Exception {
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .loginId(loginId)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(createUserRequestDto);

        //then
        mockMvc.perform(post(UserController.BASE_PATH)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail[0].field").value("loginId"))
                .andDo(print());
    }

    @DisplayName("패스워드가 형식에 어긋날 경우 가입에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"short123", "onlyalphabet", "12345678910", "blank 123123!"})
    void createUser_pw_wrong_form(String password) throws Exception {
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(password)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(createUserRequestDto);

        //then
        mockMvc.perform(post(UserController.BASE_PATH)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail[0].field").value("password"))
                .andDo(print());
    }

    @DisplayName("이메일이 형식에 어긋날 경우 가입에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"justalpha", "abc123@@google.com"})
    void createUser_email_wrong_form(String email) throws Exception {
        //given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(email)
                .name(ValidUser.NAME)
                .password("abc123123123!")
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        String content = objectMapper.writeValueAsString(createUserRequestDto);

        //then
        mockMvc.perform(post(UserController.BASE_PATH)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail[0].field").value("email"))
                .andDo(print());
    }

    @DisplayName("검색 조건에 맞는 유저가 존재하면 HTTPStatus.OK를 반환한다.")
    @Test
    void getUser_ok() throws Exception {
        mockMvc.perform(get(UserController.BASE_PATH)
                        .param("loginId", ValidUser.LOGIN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @DisplayName("검색 조건에 맞지 않는 유저가 존재하면 HTTPStatus.NOT_FOUND를 반환한다.")
    @Test
    void getUser_not_found() throws Exception {
        //given
        Mockito.lenient().doThrow(new NoSuchUserException()).when(userService)
                .getUser(any());

        //then
        mockMvc.perform(get(UserController.BASE_PATH)
                        .param("loginId", ValidUser.LOGIN_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }


    @DisplayName("로그인 했다면 자신의 정보를 볼 수 있다.")
    @Test
    void getMyInfo_login_success() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(LOGIN_KEY, new LoginUser(1L, ValidUser.NAME));

        //then
        mockMvc.perform(get(UserController.BASE_PATH + UserController.GET_MY_INFO_PATH)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("로그인하지 않았다면 자신의 정보를 볼 수 없다.")
    @Test
    void getMyInfo_not_login_fail() throws Exception {
        mockMvc.perform(get(UserController.BASE_PATH + UserController.GET_MY_INFO_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}