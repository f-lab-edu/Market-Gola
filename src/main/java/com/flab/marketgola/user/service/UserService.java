package com.flab.marketgola.user.service;

import com.flab.marketgola.user.domain.ShippingAddress;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.FindUserRequestDto;
import com.flab.marketgola.user.dto.request.JoinUserRequestDto;
import com.flab.marketgola.user.dto.response.FindUserResponseDto;
import com.flab.marketgola.user.exception.DuplicatedEmailExcepiton;
import com.flab.marketgola.user.exception.DuplicatedLoginIdExcepiton;
import com.flab.marketgola.user.exception.DuplicatedPhoneNumberException;
import com.flab.marketgola.user.exception.NoSuchUserException;
import com.flab.marketgola.user.mapper.ShippingAddressMapper;
import com.flab.marketgola.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserMapper userRepository;
    private final ShippingAddressMapper addressRepository;

    @Autowired
    public UserService(UserMapper userRepository, ShippingAddressMapper addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public User join(JoinUserRequestDto joinUserRequestDto) throws Exception {
        validateDuplication(joinUserRequestDto);

        User user = joinUserRequestDto.toUser();
        ShippingAddress shippingAddress = joinUserRequestDto.toShippingAddress(user);

        user.encryptPassword();

        userRepository.create(user);
        addressRepository.create(shippingAddress);
        return user;
    }

    private void validateDuplication(JoinUserRequestDto joinUserRequestDto) {
        validateLoginId(joinUserRequestDto.getLoginId());
        validateEmail(joinUserRequestDto.getEmail());
        validatePhoneNumber(joinUserRequestDto.getPhoneNumber());
    }

    private void validateLoginId(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent()) {
            throw new DuplicatedLoginIdExcepiton();
        }
    }

    private void validateEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicatedEmailExcepiton();
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new DuplicatedPhoneNumberException();
        }
    }

    public FindUserResponseDto findByCondition(FindUserRequestDto findUserRequestDto) {
        User user = userRepository.findByCondition(findUserRequestDto)
                .orElseThrow(NoSuchUserException::new);

        return new FindUserResponseDto(user.getId());
    }
}
