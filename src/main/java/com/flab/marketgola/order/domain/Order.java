package com.flab.marketgola.order.domain;

import com.flab.marketgola.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Order {

    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private User user;

    private List<OrderProduct> orderProducts;

    @Builder
    public Order(Long id, String receiverName, String receiverPhone, String receiverAddress,
            OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime deliveredAt, User user,
            List<OrderProduct> orderProducts) {
        this.id = id;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.deliveredAt = deliveredAt;
        this.user = user;
        this.orderProducts = orderProducts;
    }
}
