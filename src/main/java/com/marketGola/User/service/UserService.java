package com.marketGola.User.service;

import com.marketGola.User.domain.User;
import com.marketGola.User.repository.mybatis.MyBatisUserRepository;
import com.marketGola.User.request.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MyBatisUserRepository userRepository;

    public void save(UserDto userDto) {
//    public User save(UserDto userDto) {
        String name = userDto.getName();
        String email = userDto.getEmail();
        User user = new User(name, email);
        userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    };
}
