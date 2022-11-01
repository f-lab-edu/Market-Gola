package com.flab.marketgola.order.constant;

import com.flab.marketgola.order.dto.request.CreateOrderRequestDto.OrderProductDto;

public class TestOrderProductFactory {

    public static final Long PRODUCT_ID = 1L;
    public static final int COUNT = 1;

    public static OrderProductDto.OrderProductDtoBuilder generalCreateRequest() {
        return OrderProductDto.builder()
                .productId(PRODUCT_ID)
                .count(COUNT);
    }

}
