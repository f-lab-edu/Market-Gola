package com.marketgola.user.service;

import com.marketgola.user.domain.User;
import com.marketgola.user.exception.DuplicatedUserException;
import com.marketgola.user.exception.UserNotFoundException;
import com.marketgola.user.repository.mybatis.UserMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public void register(User user) {
        if (validateDuplicateMember(user.getLoginId())) {
            throw new DuplicatedUserException("LOGIN ID ALREADY TAKEN");
        }
        userMapper.save(user);
    }

    public Boolean validateDuplicateMember(String loginId) {
        Optional<User> found = userMapper.findByLoginId(loginId);
        return found.isPresent();
    }

    public User findByIdOrElseThrowUserNotFound(String loginId) {
        return userMapper.findByLoginId(loginId)
                .orElseThrow(() -> new UserNotFoundException("USER DOES NOT EXIST"));
    }
}
