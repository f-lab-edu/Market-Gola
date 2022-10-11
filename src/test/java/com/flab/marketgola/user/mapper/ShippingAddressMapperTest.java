package com.flab.marketgola.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.user.ValidUser;
import com.flab.marketgola.user.domain.Gender;
import com.flab.marketgola.user.domain.ShippingAddress;
import com.flab.marketgola.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("unit")
@Transactional
@SpringBootTest(classes = TestRedisConfiguration.class)
class ShippingAddressMapperTest {

    @Autowired
    private ShippingAddressMapper shippingAddressMapper;
    @Autowired
    private UserMapper userMapper;

    @DisplayName("정상적으로 주소가 저장된다.")
    @Test
    void insert() {
        //given
        String addressString = ValidUser.ADDRESS;
        User user = User.builder()
                .loginId(ValidUser.LOGIN_ID)
                .email(ValidUser.EMAIL)
                .name(ValidUser.NAME)
                .password(ValidUser.PASSWORD)
                .phoneNumber(ValidUser.PHONE_NUMBER)
                .gender(Gender.MALE)
                .build();

        userMapper.insert(user);

        ShippingAddress shippingAddress = ShippingAddress.builder()
                .address(addressString)
                .isDefault(true)
                .user(user)
                .build();

        //when
        shippingAddressMapper.insert(shippingAddress);

        //then
        assertThat(shippingAddressMapper.findAllByUser(user)).hasSize(1);
    }
}