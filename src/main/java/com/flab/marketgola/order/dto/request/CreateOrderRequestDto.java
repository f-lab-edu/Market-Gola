package com.flab.marketgola.order.dto.request;

import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.user.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateOrderRequestDto {

    @NotBlank(message = "수령인 이름을 입력해주세요.")
    private String receiverName;
    @NotBlank(message = "수령인 연락처를 입력해주세요.")
    private String receiverPhone;
    @NotBlank(message = "수령인 주소를 입력해주세요.")
    private String receiverAddress;
    @Size(min = 1, message = "주문 상품을 최소 1개 이상 선택해주세요.")
    @NotNull(message = "주문 상품을 최소 1개 이상 선택해주세요.")
    @Valid
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
        @Min(value = 1, message = "주문 수량은 1개 이상이어야 합니다.")
        private int count;

        @Builder
        public OrderProductDto(Long productId, int count) {
            this.productId = productId;
            this.count = count;
        }
    }
}
