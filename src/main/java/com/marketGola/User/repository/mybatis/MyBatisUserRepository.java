package com.marketGola.User.repository.mybatis;

import com.marketGola.User.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisUserRepository  {
    private final UserMapper userMapper;

    public User save(User user) {
        log.info("userMapper class={}", userMapper.getClass());
        userMapper.save(user);
        return user;
    }

    public Optional<User> findById(Long id) {
        return userMapper.findById(id);
    }
}
