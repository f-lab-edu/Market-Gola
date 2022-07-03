package com.marketgola.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.marketgola.user.domain.User;
import com.marketgola.user.repository.mybatis.UserMapper;
import com.marketgola.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    User user1;

    @BeforeEach
    void beforeEach() {
        user1 = new User();
        user1.setId(1L);
        user1.setLoginId("user1");
        user1.setEmail("user1@gmail.com");
    }

    @Test
    @DisplayName("중복 회원인 경우 예외를 던진다")
    void validateDuplicateMember() {
        //Given
        User user2 = new User();
        user2.setLoginId("user1");
        user2.setEmail("user1@gmail1.com");

        //When
        userService.register(user1);
        // Then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.register(user2));
        assertThat(e.getMessage()).isEqualTo("LOGIN ID ALREADY TAKEN");
    }

    @Test
    @DisplayName("중복 회원이 아닐 경우 예외를 던지지 않는다")
    void validateNoDuplicateMember() {
        //Given
        User user3 = new User();
        user3.setLoginId("user3");
        user3.setEmail("user3@gmail1.com");

        //When
        userService.register(user3);

        //Then
        User foundUser = userMapper.findByLoginId(user3.getLoginId()).get();
        assertThat(foundUser).isEqualTo(user3);
    }
}
