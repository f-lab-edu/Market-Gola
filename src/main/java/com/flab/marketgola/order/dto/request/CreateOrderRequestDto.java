package com.flab.marketgola.order.dto.request;

import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderRequestDto {

    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;

    private List<OrderProductDto> products;

    @Builder
    public CreateOrderRequestDto(String receiverName, String receiverPhone, String receiverAddress,
            List<OrderProductDto> products) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.products = products;
    }

    public Order toOrder(long userId) {
        return Order.builder()
                .receiverName(receiverName)
                .receiverPhone(receiverPhone)
                .receiverAddress(receiverAddress)
                .user(User.builder().id(userId).build())
                .build();
    }

    public List<OrderProduct> toOrderProducts() {
        return products.stream()
                .map(dto -> OrderProduct.builder()
                        .product(Product.builder().id(dto.getProductId()).build())
                        .count(dto.getCount()).build()
                )
                .collect(Collectors.toList());
    }


    @Getter
    @NoArgsConstructor
    public static class OrderProductDto {

        private Long productId;
        private int count;

        @Builder
        public OrderProductDto(Long productId, int count) {
            this.productId = productId;
            this.count = count;
        }
    }
}
