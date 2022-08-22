package com.flab.marketgola.user.service;

import com.flab.marketgola.user.domain.ShippingAddress;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.request.CreateUserRequestDto;
import com.flab.marketgola.user.dto.request.GetUserRequestDto;
import com.flab.marketgola.user.dto.response.UserPrivateInfoResponseDto;
import com.flab.marketgola.user.dto.response.UserPublicInfoResponseDto;
import com.flab.marketgola.user.exception.DuplicatedEmailExcepiton;
import com.flab.marketgola.user.exception.DuplicatedLoginIdException;
import com.flab.marketgola.user.exception.DuplicatedPhoneNumberException;
import com.flab.marketgola.user.exception.NoSuchUserException;
import com.flab.marketgola.user.mapper.ShippingAddressMapper;
import com.flab.marketgola.user.mapper.UserMapper;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
    public UserPrivateInfoResponseDto createUser(CreateUserRequestDto createUserRequestDto)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        validateDuplication(createUserRequestDto);

        User user = createUserRequestDto.toUser();
        ShippingAddress shippingAddress = createUserRequestDto.toShippingAddress(user);

        user.encryptPassword();

        userRepository.insert(user);
        addressRepository.insert(shippingAddress);

        return UserPrivateInfoResponseDto.of(user);
    }

    private void validateDuplication(CreateUserRequestDto createUserRequestDto) {
        validateLoginId(createUserRequestDto.getLoginId());
        validateEmail(createUserRequestDto.getEmail());
        validatePhoneNumber(createUserRequestDto.getPhoneNumber());
    }

    private void validateLoginId(String loginId) {
        if (userRepository.findByLoginId(loginId).isPresent()) {
            throw new DuplicatedLoginIdException();
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

    public UserPublicInfoResponseDto getUser(GetUserRequestDto getUserRequestDto) {
        User user = userRepository.findByCondition(getUserRequestDto)
                .orElseThrow(NoSuchUserException::new);

        return new UserPublicInfoResponseDto(user.getLoginId(), user.getName());
    }

    public UserPrivateInfoResponseDto getMyInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(NoSuchUserException::new);

        return UserPrivateInfoResponseDto.of(user);
    }
}
