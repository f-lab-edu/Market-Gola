package com.flab.marketgola.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.marketgola.TestRedisConfiguration;
import com.flab.marketgola.order.constant.TestOrderFactory;
import com.flab.marketgola.order.dto.request.CreateOrderRequestDto;
import com.flab.marketgola.order.dto.request.CreateOrderRequestDto.OrderProductDto;
import com.flab.marketgola.order.dto.response.OrderResponseDto;
import com.flab.marketgola.order.exception.OutOfStockException;
import com.flab.marketgola.order.repository.OrderProductRepository;
import com.flab.marketgola.order.repository.OrderRepository;
import com.flab.marketgola.product.constant.TestDisplayProductFactory;
import com.flab.marketgola.product.constant.TestProductFactory;
import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.exception.NoSuchProductException;
import com.flab.marketgola.product.repository.DisplayProductRepository;
import com.flab.marketgola.product.repository.ProductRepository;
import com.flab.marketgola.user.constant.TestUserFactory;
import com.flab.marketgola.user.domain.User;
import com.flab.marketgola.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("unit")
@SpringBootTest(classes = TestRedisConfiguration.class)
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DisplayProductRepository displayProductRepository;
    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAll();
        productRepository.deleteAll();
        displayProductRepository.deleteAll();
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Transactional
    @DisplayName("정상적으로 주문을 생성할 수 있다.")
    @Test
    void createOrder() {
        //given
        //사전 데이터 준비
        DisplayProduct displayProduct = insertDisplayProduct();
        Product product = insertProduct(1, displayProduct);
        User user = insertUser();

        //주문 관련 dto 생성
        OrderProductDto orderProductDto = OrderProductDto.builder()
                .productId(product.getId())
                .count(1)
                .build();

        CreateOrderRequestDto orderRequest = TestOrderFactory.generalCreateRequest()
                .products(List.of(orderProductDto)).build();

        //when
        long orderId = orderService.createOrder(user.getId(), orderRequest);

        //then
        OrderResponseDto response = orderService.getOrderById(orderId);
        assertThat(response.getProducts()).hasSize(1);
        assertThat(response.getReceiver()).isNotNull();
    }

    @PersistenceContext
    EntityManager em;

    @Transactional
    @DisplayName("주문을 할 경우 재고가 차감된다")
    @Test
    void createOrder_subtrack_stock() {
        //given
        //사전 데이터 준비
        DisplayProduct displayProduct = insertDisplayProduct();
        Product product = insertProduct(10, displayProduct);
        User user = insertUser();

        //주문 관련 dto 생성
        OrderProductDto orderProductDto = OrderProductDto.builder()
                .productId(product.getId())
                .count(5)
                .build();

        CreateOrderRequestDto orderRequest = TestOrderFactory.generalCreateRequest()
                .products(List.of(orderProductDto)).build();

        //when
        orderService.createOrder(user.getId(), orderRequest);

        //then
        Product productAfterOrder = productRepository.findById(product.getId()).get();
        assertThat(productAfterOrder.getStock()).isEqualTo(5);
    }

    @Transactional
    @DisplayName("주문을 생성할 때, 상품의 재고가 부족하면 예외가 발생한다.")
    @Test
    void createOrder_no_stock_exception() {
        //given
        //사전 데이터 준비
        DisplayProduct displayProduct = insertDisplayProduct();
        Product product = insertProduct(0, displayProduct);
        User user = insertUser();

        //주문 관련 dto 생성
        OrderProductDto orderProductDto = OrderProductDto.builder()
                .productId(product.getId())
                .count(1)
                .build();

        CreateOrderRequestDto orderRequest = TestOrderFactory.generalCreateRequest()
                .products(List.of(orderProductDto)).build();

        //then
        assertThatThrownBy(() -> orderService.createOrder(user.getId(), orderRequest))
                .isInstanceOf(OutOfStockException.class);
    }

    @Transactional
    @DisplayName("주문하고자 하는 상품이 없는 경우 예외가 발생한다.")
    @Test
    void createOrder_no_product_exception() {
        //given
        //사전 데이터 준비
        DisplayProduct displayProduct = insertDisplayProduct();
        Product product = insertProduct(10, displayProduct);
        User user = insertUser();

        //주문 관련 dto 생성
        OrderProductDto orderProductDto = OrderProductDto.builder()
                .productId(100000L)
                .count(1)
                .build();

        CreateOrderRequestDto orderRequest = TestOrderFactory.generalCreateRequest()
                .products(List.of(orderProductDto)).build();

        //then
        assertThatThrownBy(() -> orderService.createOrder(user.getId(), orderRequest))
                .isInstanceOf(NoSuchProductException.class);
    }

    Exception occurredException = null;

    @DisplayName("동시에 주문을 하더라도 재고를 초과해서 주문이 이루어지지 않는다.")
    @Test
    void createOrder_concurrent_order_not_over_stock() throws InterruptedException {
        //given
        //사전 데이터 준비
        DisplayProduct displayProduct = insertDisplayProduct();
        Product product = insertProduct(100, displayProduct);
        User user = insertUser();

        //주문 관련 dto 생성
        OrderProductDto orderProductDto = OrderProductDto.builder()
                .productId(product.getId())
                .count(1)
                .build();

        CreateOrderRequestDto orderRequest = TestOrderFactory.generalCreateRequest()
                .products(List.of(orderProductDto)).build();

        //then
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            Thread thread = new Thread(() -> {
                try {
                    orderService.createOrder(user.getId(), orderRequest);
                } catch (Exception e) {
                    occurredException = e;
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        assertThat(occurredException).isInstanceOf(OutOfStockException.class);
    }

    @DisplayName("동시에 주문을 하더라도 주문 수량과 차감된 재고가 정확히 일치한다.")
    @Test
    void createOrder_concurrent_order_ok() throws InterruptedException {
        //given
        //사전 데이터 준비
        DisplayProduct displayProduct = insertDisplayProduct();
        Product product = insertProduct(100, displayProduct);
        User user = insertUser();

        //주문 관련 dto 생성
        OrderProductDto orderProductDto = OrderProductDto.builder()
                .productId(product.getId())
                .count(1)
                .build();

        CreateOrderRequestDto orderRequest = TestOrderFactory.generalCreateRequest()
                .products(List.of(orderProductDto)).build();

        //then
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> orderService.createOrder(user.getId(), orderRequest));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        int stock = productRepository.findById(product.getId()).get().getStock();
        assertThat(stock).isZero();
    }

    private User insertUser() {
        User user = TestUserFactory.generalUser().build();
        userRepository.save(user);
        return user;
    }

    private Product insertProduct(int stock, DisplayProduct displayProduct) {
        Product product = TestProductFactory.generalProduct()
                .stock(stock)
                .displayProduct(displayProduct)
                .build();
        productRepository.save(product);
        return product;
    }

    private DisplayProduct insertDisplayProduct() {
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct().build();
        displayProductRepository.save(displayProduct);
        return displayProduct;
    }
}