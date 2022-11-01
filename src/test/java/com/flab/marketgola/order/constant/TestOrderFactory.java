package com.flab.marketgola.order.constant;

import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderStatus;
import com.flab.marketgola.order.dto.request.CreateOrderRequestDto;
import com.flab.marketgola.user.domain.User;
import java.util.List;

public class TestOrderFactory {

    public static final long ORDER_ID = 1L;
    public static final String RECEIVER_NAME = "홍길동";
    public static final String RECEIVER_PHONE = "01012345678";
    public static final String RECEIVER_ADDRESS = "서울시 강남구 역삼동 건영빌라 101동 101호";
    public static final OrderStatus ORDER_STATUS = OrderStatus.PROCESSING;
    public static final long USER_ID = 1L;

    public static Order.OrderBuilder generalOrder() {
        return Order.builder()
                .receiverName(RECEIVER_NAME)
                .receiverPhone(RECEIVER_PHONE)
                .receiverAddress(RECEIVER_ADDRESS)
                .orderStatus(ORDER_STATUS)
                .user(User.builder().id(USER_ID).build());
    }

    public static CreateOrderRequestDto.CreateOrderRequestDtoBuilder generalCreateRequest() {

        return CreateOrderRequestDto.builder()
                .receiverName(RECEIVER_NAME)
                .receiverPhone(RECEIVER_PHONE)
                .receiverAddress(RECEIVER_ADDRESS)
                .products(List.of(TestOrderProductFactory.generalCreateRequest().build()));
    }
}
