package com.flab.marketgola.order.dto.response;

import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponseDto {

    private long orderId;
    private String sender;
    private OrderStatus orderStatus;
    private ReceiverDto receiver;
    private List<ProductDto> products;

    @Builder
    public OrderResponseDto(long orderId, String sender, OrderStatus orderStatus,
            ReceiverDto receiver,
            List<ProductDto> products) {
        this.orderId = orderId;
        this.sender = sender;
        this.orderStatus = orderStatus;
        this.receiver = receiver;
        this.products = products;
    }

    public static OrderResponseDto of(Order order) {
        List<ProductDto> products = order.getOrderProducts().stream()
                .map(orderProduct -> {
                    long productId = orderProduct.getProduct().getId();
                    String productName = orderProduct.getProduct().getName();
                    int price = orderProduct.getProduct().getPrice();
                    int count = orderProduct.getCount();

                    return new ProductDto(productId, productName, price, count);
                })
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .sender(order.getUser().getName())
                .orderStatus(order.getOrderStatus())
                .receiver(new ReceiverDto(order))
                .products(products)
                .build();
    }

    @Getter
    static class ReceiverDto {

        private String name;
        private String phone;
        private String address;

        public ReceiverDto(Order order) {
            this.name = order.getReceiverName();
            this.phone = order.getReceiverPhone();
            this.address = order.getReceiverAddress();
        }
    }

    @Getter
    static class ProductDto {

        private long productId;
        private String name;
        private int price;
        private int count;

        public ProductDto(long productId, String productName, int price, int count) {
            this.productId = productId;
            this.name = productName;
            this.price = price;
            this.count = count;
        }
    }
}
