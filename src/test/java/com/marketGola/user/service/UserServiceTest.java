package com.marketgola.user.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    User user;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setId(1L);
        user.setName("user1");
        user.setEmail("user1@gmail.com");

    }

    @Test
    @DisplayName("회원 가입 성공")
    void register() throws Exception {

        //When
        //ID가 1L인 유저를 찾는다면, 위의 user 객체를 반환.
        when(userService.findByIdOrElseNull(1L)).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.findByIdOrElseNull(1L);
        assertEquals("user1", foundUser.get().getName());
    }

    @Test
    @DisplayName("중복 회원이 아닌 경우 예외를 던지지 않는다")
    void validateNoDup() {

        //기존 유저 user1 과 다른 이름의 user3 생성.
        User user3 = new User();
        user3.setName("user3");
        user3.setEmail("user3@gmail1.com");

        when(userMapper.findByName("user3")).thenReturn(false);
        assertDoesNotThrow(() -> {
            userService.validateDuplicateMember(user3.getName());
        });
        verify(userMapper).findByName("user3");
    }

    @Test
    @DisplayName("중복 회원인 경우 예외를 던진다")
    void validateDup() {

        //user1 과 동일한 이름의 user2 생성.
        User user2 = new User();
        user2.setName("user1");
        user2.setEmail("user1@gmail1.com");

        //validate method 인자에 user2 입력 시, IllegalStateException 를 던진다.
        when(userMapper.findByName("user1")).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            userService.validateDuplicateMember(user2.getName());
        });

        verify(userMapper).findByName("user1");
    }

}