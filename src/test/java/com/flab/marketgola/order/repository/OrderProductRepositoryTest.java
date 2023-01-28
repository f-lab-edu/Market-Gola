package com.flab.marketgola.order.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.order.constant.TestOrderFactory;
import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.repository.DisplayProductRepository;
import com.flab.marketgola.product.repository.ProductRepository;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("unit")
@SpringBootTest(classes = TestRedisConfiguration.class)
class OrderProductRepositoryTest {

    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DisplayProductRepository displayProductRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    @DisplayName("정상적으로 주문 상품 기록을 생성할 수 있다.")
    @Test
    void save() {
        //given
        //상품 사전 등록
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();
        displayProductRepository.save(displayProduct);
        Product product = TestProductFactory.generalProduct().displayProduct(displayProduct)
                .build();
        productRepository.save(product);

        //유저 사전 등록
        User user = TestUserFactory.generalUser().build();
        userRepository.save(user);

        //주문 사전 등록
        Order order = TestOrderFactory.generalOrder().user(user).build();
        orderRepository.save(order);

        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .order(order)
                .build();

        //when
        orderProductRepository.save(orderProduct);

        //then
        assertThat(orderProductRepository.findById(orderProduct.getId())).isNotEmpty();
    }
}