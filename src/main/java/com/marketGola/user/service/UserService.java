package com.marketgola.user.service;

import com.marketgola.user.domain.User;
import com.marketgola.user.repository.mybatis.UserMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public void register(User user) {
        userMapper.save(user);
    }

    public void validateDuplicateMember(String name) {
        boolean isExistingName = userMapper.findByName(name);
        if (isExistingName) {
            throw new IllegalStateException("name ALREADY TAKEN");
        }
    }


    public User findByIdOrElseThrow(Long id) {
        return userMapper.findById(id)
                .orElseThrow(() -> new IllegalStateException("user does not exist"));
    }

    public Optional<User> findByIdOrElseNull(Long id) {
        return userMapper.findById(id);
    }

}
