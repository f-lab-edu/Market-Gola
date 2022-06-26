package com.marketgola.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.marketgola.user.domain.User;
import com.marketgola.user.repository.mybatis.UserMapper;
import com.marketgola.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserIntegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Test
    @DisplayName("회원 가입 성공")
    void register() {
        //given
        User user = new User("JK", "jk@gmail3.com");

        //when
        userMapper.save(user);

        //then
        User foundUser = userMapper.findById(user.getId()).get();
        assertEquals(user, foundUser);
    }

    @Test
    @DisplayName("중복 회원인 경우 예외를 던진다")
    void validateDuplicateMember() {

        User user1 = new User();
        user1.setName("user1");
        user1.setEmail("user1@gmail1.com");

        User user2 = new User();
        user2.setName("user1");
        user2.setEmail("user1@gmail1.com");

        User user3 = new User();
        user3.setName("user3");
        user3.setEmail("user3@gmail1.com");

        //when
        userService.register(user1);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.validateDuplicateMember(user2.getName()));
        assertThat(e.getMessage()).isEqualTo("name ALREADY TAKEN");

        userService.validateDuplicateMember(user3.getName());
    }
}
