package com.flab.marketgola.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.UserJoinDto;
import com.flab.marketgola.user.exception.DuplicatedEmailExcepiton;
import com.flab.marketgola.user.exception.DuplicatedLoginIdExcepiton;
import com.flab.marketgola.user.exception.DuplicatedPhoneNumberException;
import com.flab.marketgola.user.mapper.ShippingAddressMapper;
import com.flab.marketgola.user.mapper.UserMapper;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("unit")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userRepository;

    @Mock
    private ShippingAddressMapper shippingAddressRepository;

    @DisplayName("유저가 정상적으로 가입할 수 있다.")
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

        //when
        User user = userService.join(userJoinDto);
    }

    @DisplayName("비밀번호가 암호화되어 저장되어야 한다.")
    @Test
    void join_encrypt() throws Exception {
        //given
        UserJoinDto joiningUser = UserJoinDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();
        //when
        User joinedUser = userService.join(joiningUser);

        //then
        assertThat(joinedUser.getPassword()).isNotEqualTo(ValidUser.PASSWORD);
    }

    @DisplayName("아이디 또는 이메일 또는 핸드폰 번호가 중복될 경우 가입에 실패한다.")
    @ParameterizedTest
    @MethodSource("join_duplication_arguments")
    void join_duplication(String errorMessage, Class<Exception> exceptionClass) {
        //given
        Mockito.lenient().doThrow(new DuplicateKeyException(errorMessage)).when(userRepository)
                .create(any());

        UserJoinDto joiningUser = UserJoinDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        //when
        assertThatThrownBy(() -> userService.join(joiningUser))
                .isInstanceOf(exceptionClass);
    }

    private static Stream<Arguments> join_duplication_arguments() {
        return Stream.of(
                Arguments.of("user.login_id_UNIQUE", DuplicatedLoginIdExcepiton.class),
                Arguments.of("user.email_UNIQUE", DuplicatedEmailExcepiton.class),
                Arguments.of("user.phone_number_UNIQUE", DuplicatedPhoneNumberException.class)
        );
    }


    @DisplayName("중복된 아이디는 true를 리턴한다.")
    @Test
    void isDuplicatedLoginId_true() {
        //given
        Mockito.when(userRepository.findByLoginId(ValidUser.LOGIN_ID))
                .thenReturn(Optional.of(new User()));

        //when
        boolean result = userService.isDuplicatedLoginId(ValidUser.LOGIN_ID);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("중복되지 않은 아이디는 false를 리턴한다.")
    @Test
    void isDuplicatedLoginId_false() {
        //given
        Mockito.when(userRepository.findByLoginId(ValidUser.LOGIN_ID))
                .thenReturn(Optional.empty());

        //when
        boolean result = userService.isDuplicatedLoginId(ValidUser.LOGIN_ID);

        //then
        assertThat(result).isFalse();
    }


    @DisplayName("중복된 이메일은 true를 리턴한다.")
    @Test
    void isDuplicatedEmail_true() {
        //given
        Mockito.when(userRepository.findByEmail(ValidUser.EMAIL))
                .thenReturn(Optional.of(new User()));

        //when
        boolean result = userService.isDuplicatedEmail(ValidUser.EMAIL);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("중복되지 않은 이메일은 false를 리턴한다.")
    @Test
    void isDuplicatedEmail_false() {
        //given
        Mockito.when(userRepository.findByEmail(ValidUser.EMAIL))
                .thenReturn(Optional.empty());

        //when
        boolean result = userService.isDuplicatedEmail(ValidUser.EMAIL);

        //then
        assertThat(result).isFalse();
    }

    @DisplayName("중복된 핸드폰 번호는 true를 리턴한다.")
    @Test
    void isDuplicatedPhoneNumber_true() {
        //given
        Mockito.when(userRepository.findByPhoneNumber(ValidUser.PHONE_NUMBER))
                .thenReturn(Optional.of(new User()));

        //when
        boolean result = userService.isDuplicatedPhoneNumber(ValidUser.PHONE_NUMBER);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("중복되지 않은 핸드폰 번호는 false를 리턴한다.")
    @Test
    void isDuplicatedPhoneNumber_false() {
        //given
        Mockito.when(userRepository.findByPhoneNumber(ValidUser.PHONE_NUMBER))
                .thenReturn(Optional.empty());

        //when
        boolean result = userService.isDuplicatedPhoneNumber(ValidUser.PHONE_NUMBER);

        //then
        assertThat(result).isFalse();
    }
}