package com.marketGola.user.repository;

import com.marketGola.user.domain.User;
import com.marketGola.user.repository.mybatis.MyBatisUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    MyBatisUserRepository userRepository;

    @Test
    void save() {
        //given
        User user = new User("JK", "jk@gmail3.com");

        //when
        User savedUser = userRepository.save(user);

        //then
        Optional<User> findUser = userRepository.findById(user.getId());
            assertThat(findUser.orElse(null)).isEqualTo(savedUser);
        }
//        if (findUser.isPresent()) {
//            assertThat(findUser.get()).isEqualTo(savedUser);
//        }
    }
