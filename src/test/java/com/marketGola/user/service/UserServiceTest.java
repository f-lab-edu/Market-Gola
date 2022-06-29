package com.marketgola.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.marketgola.user.domain.User;
import com.marketgola.user.repository.mybatis.UserMapper;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserService userService;

    User user1;

    @BeforeEach
    void beforeEach() {
        user1 = new User();
        user1.setId(1L);
        user1.setLoginId("user1");
        user1.setEmail("user1@gmail.com");
    }

    @Test
    @DisplayName("중복 유저임을 확인한다")
    void duplicateMember() {
        when(userMapper.findByLoginId("user1")).thenReturn(Optional.of(user1));
        assertThat(userService.validateDuplicateMember("user1")).isTrue();
        verify(userMapper).findByLoginId("user1");
    }

    @Test
    @DisplayName("중복 유저가 아님을 확인한다")
    void noDuplicateMember() {
        when(userMapper.findByLoginId("user2")).thenReturn(Optional.empty());
        assertThat(userService.validateDuplicateMember("user2")).isFalse();
        verify(userMapper).findByLoginId("user2");
    }

    @Test
    @DisplayName("중복 회원이 아닌 경우, 예외 없이 가입에 성공한다")
    void registerSuccess() {

        //Given
        when(userMapper.findByLoginId("user2")).thenReturn(Optional.empty());

        User user2 = new User();
        user2.setId(2L);
        user2.setLoginId("user2");
        user2.setEmail("user2@gmail.com");

        //When
        userService.register(user2);
        //Then
        verify(userMapper).save(user2);
        verify(userMapper).findByLoginId("user2");
    }

    @Test
    @DisplayName("중복 회원인 경우 예외를 던지고 가입에 실패한다")
    void validateDup() {

        //Given: user1 과 동일한 이름의 user3 생성.
        User user3 = new User();
        user3.setId(3L);
        user3.setLoginId("user1");
        user3.setEmail("user1@gmail1.com");

        //When: validate method 인자에 user3의 Id 검색 시 - 동일한 Id 인 user1를 반환한다
        when(userMapper.findByLoginId(user3.getLoginId())).thenReturn(Optional.of(user1));

        //Then: 가입 시도 시 IllegalStateException 을 반환한다.
        assertThrows(IllegalStateException.class, () -> {
            userService.register(user3);
        });
        verify(userMapper).findByLoginId("user1");
        verify(userMapper, never()).save(user3);
    }

}