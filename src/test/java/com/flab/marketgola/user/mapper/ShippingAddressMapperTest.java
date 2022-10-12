package com.flab.marketgola.user.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.user.constant.TestShippingAddressFactory;
import com.flab.marketgola.user.constant.TestUserFactory;
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
        String addressString = TestUserFactory.ADDRESS;
        User user = TestUserFactory.generalUser().build();

        userMapper.insert(user);

        ShippingAddress shippingAddress = TestShippingAddressFactory.generalShippingAddress()
                .user(user)
                .build();

        //when
        shippingAddressMapper.insert(shippingAddress);

        //then
        assertThat(shippingAddressMapper.findAllByUser(user)).hasSize(1);
    }
}