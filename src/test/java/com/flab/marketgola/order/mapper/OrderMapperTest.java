package com.flab.marketgola.order.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.order.constant.TestOrderFactory;
import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("unit")
@SpringBootTest(classes = TestRedisConfiguration.class)
class OrderMapperTest {

    @Autowired
    OrderMapper orderRepository;

    @Autowired
    UserMapper userRepository;

    @DisplayName("정상적으로 주문을 생성할 수 있다.")
    @Test
    void insert() {
        //given
        User user = TestUserFactory.generalUser().id(1L).build();
        userRepository.insert(user);
        Order order = TestOrderFactory.generalOrder().user(user).build();

        //when
        orderRepository.insert(order);

        //then
        assertThat(orderRepository.findById(order.getId())).isNotEmpty();
    }
}