package com.flab.marketgola.order.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.order.constant.TestOrderFactory;
import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.mapper.DisplayProductMapper;
import com.flab.marketgola.product.mapper.ProductMapper;
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
class OrderProductMapperTest {

    @Autowired
    OrderProductMapper orderProductRepository;
    @Autowired
    ProductMapper productRepository;
    @Autowired
    DisplayProductMapper displayProductRepository;
    @Autowired
    OrderMapper orderRepository;
    @Autowired
    UserMapper userRepository;

    @DisplayName("정상적으로 주문 상품 기록을 생성할 수 있다.")
    @Test
    void insert() {
        //given
        //상품 사전 등록
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();
        displayProductRepository.insert(displayProduct);
        Product product = TestProductFactory.generalProduct().displayProduct(displayProduct)
                .build();
        productRepository.insert(product);

        //유저 사전 등록
        User user = TestUserFactory.generalUser().build();
        userRepository.insert(user);

        //주문 사전 등록
        Order order = TestOrderFactory.generalOrder().user(user).build();
        orderRepository.insert(order);

        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .order(order)
                .build();

        //when
        Long orderProductId = orderProductRepository.insert(orderProduct);

        //then
        assertThat(orderProductRepository.findById(orderProductId)).isNotEmpty();
    }
}