package com.flab.marketgola.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.CreateUserRequestDto;
import com.flab.marketgola.user.dto.request.GetUserRequestDto;
import com.flab.marketgola.user.exception.DuplicatedEmailExcepiton;
import com.flab.marketgola.user.exception.DuplicatedLoginIdException;
import com.flab.marketgola.user.exception.DuplicatedPhoneNumberException;
import com.flab.marketgola.user.exception.NoSuchUserException;
import com.flab.marketgola.user.mapper.ShippingAddressMapper;
import com.flab.marketgola.user.mapper.UserMapper;
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
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userRepository;

    @Mock
    private ShippingAddressMapper shippingAddressRepository;

    @DisplayName("유저가 정상적으로 가입할 수 있다.")
    @Test
    void create() throws Exception {
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

        //when
        userService.createUser(createUserRequestDto);
    }

    @DisplayName("아이디가 중복될 경우 가입에 실패한다.")
    @Test
    void create_id_duplication() {
        //given
        Mockito.when(userRepository.findByLoginId(ValidUser.LOGIN_ID))
                .thenReturn(Optional.of(new User()));

        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        //when
        assertThatThrownBy(() -> userService.createUser(createUserRequestDto))
                .isInstanceOf(DuplicatedLoginIdException.class);
    }


    @DisplayName("이메일이 중복될 경우 가입에 실패한다.")
    @Test
    void create_email_duplication() {
        //given
        Mockito.when(userRepository.findByEmail(ValidUser.EMAIL))
                .thenReturn(Optional.of(new User()));

        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        //when
        assertThatThrownBy(() -> userService.createUser(createUserRequestDto))
                .isInstanceOf(DuplicatedEmailExcepiton.class);
    }


    @DisplayName("핸드폰 번호가 중복될 경우 가입에 실패한다.")
    @Test
    void create_phone_number_duplication() {
        //given
        Mockito.when(userRepository.findByPhoneNumber(ValidUser.PHONE_NUMBER))
                .thenReturn(Optional.of(new User()));

        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .address(ValidUser.ADDRESS)
                .build();

        //when
        assertThatThrownBy(() -> userService.createUser(createUserRequestDto))
                .isInstanceOf(DuplicatedPhoneNumberException.class);
    }

    @DisplayName("조건에 맞는 유저가 없으면 예외를 던진다.")
    @Test
    void getByCondition_fail() {
        //given
        Mockito.when(userRepository.findByCondition(any())).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> userService.getUser(new GetUserRequestDto()))
                .isInstanceOf(NoSuchUserException.class);
    }
}