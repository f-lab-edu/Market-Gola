package com.marketGola.User.domain;

import com.marketGola.User.repository.mybatis.MyBatisUserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    MyBatisUserRepository userRepository;


    @Test
    void save() {
        //given
        User user = new User("JK", "jk@gmail1.com" );

        //when
        User savedUser = userRepository.save(user);

        //then
        User findUser = userRepository.findById(user.getId()).get();
        assertThat(findUser).isEqualTo(savedUser);
    }
}
