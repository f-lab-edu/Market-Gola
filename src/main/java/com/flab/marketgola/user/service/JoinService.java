package com.flab.marketgola.user.service;

import com.flab.marketgola.user.domain.ShippingAddress;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.dto.UserJoinDto;
import com.flab.marketgola.user.exception.DuplicatedEmailExcepiton;
import com.flab.marketgola.user.exception.DuplicatedLoginIdExcepiton;
import com.flab.marketgola.user.exception.DuplicatedPhoneNumberException;
import com.flab.marketgola.user.mapper.ShippingAddressMapper;
import com.flab.marketgola.user.mapper.UserMapper;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JoinService {

    private final UserMapper userRepository;
    private final ShippingAddressMapper addressRepository;

    @Autowired
    public JoinService(UserMapper userRepository, ShippingAddressMapper addressRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public User join(UserJoinDto userJoinDto) {
        User user = userJoinDto.toUser();
        ShippingAddress shippingAddress = userJoinDto.toShippingAddress(user);

        user.encryptPassword();

        try {
            userRepository.create(user);
        } catch (DuplicateKeyException e) {
            throw identifyError(e);
        }
        addressRepository.create(shippingAddress);
        return user;
    }

    private RuntimeException identifyError(DuplicateKeyException e) {
        String errorField = extractErrorField(e);
        switch (errorField) {
            case "login_id":
                return new DuplicatedLoginIdExcepiton(LogLevel.DEBUG);
            case "email":
                return new DuplicatedEmailExcepiton(LogLevel.DEBUG);
            case "phone_number":
                return new DuplicatedPhoneNumberException(LogLevel.DEBUG);
            default:
                return e;
        }
    }

    private String extractErrorField(DuplicateKeyException e) {
        Pattern pattern = Pattern.compile("(?<=user.)[a-z_]+(?=_UNIQUE)");
        Matcher matcher = pattern.matcher(e.getMessage());
        matcher.find();
        return matcher.group();
    }

    public boolean isDuplicatedLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }

    public boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isDuplicatedPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
}
