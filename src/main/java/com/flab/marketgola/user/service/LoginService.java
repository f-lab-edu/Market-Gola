package com.flab.marketgola.user.service;

import com.flab.marketgola.user.domain.LoginUser;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.LoginRequestDto;
import com.flab.marketgola.user.exception.LoginFailException;
import com.flab.marketgola.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserMapper userRepository;

    public LoginUser login(LoginRequestDto loginRequestDTO) {
        User loginUser = userRepository.findByLoginId(loginRequestDTO.getLoginId())
                .filter(user -> user.isPasswordEqual(loginRequestDTO.getPassword()))
                .orElseThrow(LoginFailException::new);

        return new LoginUser(loginUser.getId(), loginUser.getName());
    }
}
