package com.flab.marketgola.order.domain;

import com.flab.marketgola.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderProduct {

    private Long id;
    private int count;
    private Order order;
    private Product product;

    @Builder
    public OrderProduct(Long id, int count, Order order, Product product) {
        this.id = id;
        this.count = count;
        this.order = order;
        this.product = product;
    }
}
