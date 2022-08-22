package com.flab.marketgola.user.service;

import com.flab.marketgola.user.domain.LoginUser;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.LoginRequestDto;
import com.flab.marketgola.user.exception.LoginFailException;
import com.flab.marketgola.user.mapper.UserMapper;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserMapper userRepository;

    public LoginUser login(LoginRequestDto loginRequestDTO)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        User loginUser = userRepository.findByLoginId(loginRequestDTO.getLoginId())
                .orElseThrow(() -> new LoginFailException());

        if (loginUser.isPasswordEqual(loginRequestDTO.getPassword())) {
            return new LoginUser(loginUser.getId(), loginUser.getName());
        } else {
            throw new LoginFailException();
        }
    }
}
