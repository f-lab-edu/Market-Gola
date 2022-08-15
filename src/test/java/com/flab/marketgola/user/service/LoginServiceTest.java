package com.flab.marketgola.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.UserLoginDto;
import com.flab.marketgola.user.exception.LoginFailException;
import com.flab.marketgola.user.mapper.UserMapper;
import com.flab.marketgola.user.util.PasswordEncryptionUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class LoginServiceTest {

    @InjectMocks
    LoginService loginService;
    @Mock
    UserMapper userRepository;

    @DisplayName("아이디가 존재하고 비밀번호가 일치하면 로그인에 성공한다.")
    @Test
    void login() {
        //given
        User user = User.builder()
                .loginId(ValidUser.LOGIN_ID)
                .password(PasswordEncryptionUtil.encrypt(ValidUser.PASSWORD))
                .name(ValidUser.NAME)
                .build();

        Mockito.when(userRepository.findByLoginId(ValidUser.LOGIN_ID))
                .thenReturn(Optional.ofNullable(user));

        //when
        UserLoginDto userLoginDto = new UserLoginDto(ValidUser.LOGIN_ID, ValidUser.PASSWORD);
        String loginUserName = loginService.login(userLoginDto);

        //then
        assertThat(loginUserName).isEqualTo(ValidUser.NAME);
    }

    @DisplayName("아이디가 존재하지 않으면 로그인에 실패한다.")
    @Test
    void login_no_id_fail() {
        //given
        Mockito.when(userRepository.findByLoginId(ValidUser.LOGIN_ID)).thenReturn(Optional.empty());

        //when
        UserLoginDto userLoginDto = new UserLoginDto(ValidUser.LOGIN_ID, ValidUser.PASSWORD);

        //then
        assertThatThrownBy(() -> loginService.login(userLoginDto))
                .isInstanceOf(LoginFailException.class);
    }

    @DisplayName("비밀번호가 틀리면 로그인에 실패한다.")
    @Test
    void login_password_no_match_fail() {
        //given
        User user = User.builder()
                .loginId(ValidUser.LOGIN_ID)
                .password(PasswordEncryptionUtil.encrypt(ValidUser.PASSWORD))
                .build();

        Mockito.when(userRepository.findByLoginId(ValidUser.LOGIN_ID))
                .thenReturn(Optional.ofNullable(user));

        //when
        UserLoginDto userLoginDto = new UserLoginDto(ValidUser.LOGIN_ID, "wrongpassword123!");

        //then
        assertThatThrownBy(() -> loginService.login(userLoginDto))
                .isInstanceOf(LoginFailException.class);
    }
}