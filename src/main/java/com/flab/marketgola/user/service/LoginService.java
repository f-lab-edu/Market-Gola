package com.flab.marketgola.user.service;

import com.flab.marketgola.user.dto.UserLoginDto;
import com.flab.marketgola.user.exception.LoginFailException;
import com.flab.marketgola.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserMapper userRepository;

    public String login(UserLoginDto userLoginDTO) {
        return userRepository.findByLoginId(userLoginDTO.getLoginId())
                .filter(user -> user.isPasswordEqual(userLoginDTO.getPassword()))
                .orElseThrow(LoginFailException::new)
                .getName();
    }
}
