package com.flab.marketgola.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.GetUserRequestDto;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("unit")
@Transactional
@SpringBootTest(classes = TestRedisConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저를 등록할 수 있다.")
    void create() {
        //given
        User user = TestUserFactory.generalUser().build();

        //when
        userRepository.save(user);

        //then
        assertThat(userRepository.findByLoginId(TestUserFactory.LOGIN_ID)).isPresent();
    }

    @Test
    @DisplayName("로그인 아이디로 유저를 찾을 수 있다.")
    void findByLoginId() {
        //given
        User user = TestUserFactory.generalUser().build();
        userRepository.save(user);

        //when
        User userInfo = userRepository.findByLoginId(TestUserFactory.LOGIN_ID).get();

        //then
        assertThat(userInfo.getLoginId()).isEqualTo(TestUserFactory.LOGIN_ID);
    }

    @Test
    @DisplayName("유저를 삭제할 수 있다.")
    void delete() {
        //given
        User user = TestUserFactory.generalUser().build();
        userRepository.save(user);

        //when
        userRepository.delete(user);
        boolean isEmpty = userRepository.findByLoginId(user.getLoginId()).isEmpty();

        //then
        assertThat(isEmpty).isTrue();
    }

    @Test
    @DisplayName("아이디 또는 이메일 또는 전화번호로 유저를 찾을 수 있다.")
    void findByCondition() {
        //given
        User user = TestUserFactory.generalUser().build();
        userRepository.save(user);

        //when
        Optional<User> userFoundByLoginId = userRepository.findByCondition(
                GetUserRequestDto.builder().loginId(TestUserFactory.LOGIN_ID).build());
        Optional<User> userFoundByEmail = userRepository.findByCondition(
                GetUserRequestDto.builder().email(TestUserFactory.EMAIL).build());
        Optional<User> userFoundByPhoneNumber = userRepository.findByCondition(
                GetUserRequestDto.builder().phoneNumber(TestUserFactory.PHONE_NUMBER).build());

        //then
        assertThat(userFoundByLoginId).isNotEmpty();
        assertThat(userFoundByEmail).isNotEmpty();
        assertThat(userFoundByPhoneNumber).isNotEmpty();
    }
}