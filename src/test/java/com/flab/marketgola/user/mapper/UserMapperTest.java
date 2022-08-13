package com.flab.marketgola.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.domain.Gender;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.FindUserRequestDto;
import com.flab.marketgola.user.dto.request.UserUpdateDto;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("unit")
@Transactional
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void init() {

    }

    @Test
    @DisplayName("유저를 등록할 수 있다.")
    void create() {
        //given
        String loginId = ValidUser.LOGIN_ID;

        User user = User.builder()
                .loginId(loginId)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(Gender.MALE)
                .build();

        //when
        userMapper.create(user);

        //then
        assertThat(userMapper.findByLoginId(loginId)).isPresent();
    }

    @Test
    @DisplayName("로그인 아이디로 유저를 찾을 수 있다.")
    void findByLoginId() {
        //given
        User user = User.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(Gender.MALE)
                .build();

        userMapper.create(user);

        //when
        User userInfo = userMapper.findByLoginId(ValidUser.LOGIN_ID).get();
        System.out.println(userInfo);
        //then
        assertThat(userInfo.getLoginId()).isEqualTo(ValidUser.LOGIN_ID);
    }

    @Test
    @DisplayName("유저 정보 업데이트를 할 수 있다.")
    void update() {
        //given
        User user = User.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(Gender.MALE)
                .build();

        userMapper.create(user);
        UserUpdateDto updateParam = new UserUpdateDto();
        updateParam.setName("김유미");
        updateParam.setGender(Gender.FEMALE);

        //when
        userMapper.update(user.getId(), updateParam);
        User userInfo = userMapper.findByLoginId(user.getLoginId()).get();

        //then
        assertThat(userInfo.getName()).isEqualTo("김유미");
        assertThat(userInfo.getGender()).isEqualTo(Gender.FEMALE);
    }

    @Test
    @DisplayName("유저를 삭제할 수 있다.")
    void delete() {
        //given
        User user = User.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(Gender.MALE)
                .build();
        userMapper.create(user);

        //when
        userMapper.delete(user.getId());
        boolean isEmpty = userMapper.findByLoginId(user.getLoginId()).isEmpty();

        //then
        assertThat(isEmpty).isTrue();
    }


    @Test
    @DisplayName("아이디 또는 이메일 또는 전화번호로 유저를 찾을 수 있다.")
    void findByCondition() {
        //given
        User user = User.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(Gender.MALE)
                .build();

        userMapper.create(user);

        //when
        Optional<User> userFoundByLoginId = userMapper.findByCondition(
                FindUserRequestDto.builder().loginId(ValidUser.LOGIN_ID).build());
        Optional<User> userFoundByEmail = userMapper.findByCondition(
                FindUserRequestDto.builder().email(ValidUser.EMAIL).build());
        Optional<User> userFoundByPhoneNumber = userMapper.findByCondition(
                FindUserRequestDto.builder().phoneNumber(ValidUser.PHONE_NUMBER).build());

        //then
        assertThat(userFoundByLoginId).isNotEmpty();
        assertThat(userFoundByEmail).isNotEmpty();
        assertThat(userFoundByPhoneNumber).isNotEmpty();
    }
}