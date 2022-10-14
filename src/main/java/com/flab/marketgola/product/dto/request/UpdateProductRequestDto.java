package com.flab.marketgola.product.dto.request;

import com.flab.marketgola.product.domain.Product;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateProductRequestDto {

    private Long id;
    @NotBlank(message = "상품의 이름을 입력하지 않았습니다.")
    @Size(min = 1, max = 100, message = "상품의 이름은 1자 이상 100자 이하로 입력해주세요.")
    private String name;
    @Min(value = 100, message = "상품의 가격은 100원 이상이어야 합니다.")
    private int price;
    @Min(value = 0, message = "상품의 재고는 0개 이상이어야 합니다.")
    private int stock;

    private boolean isDeleted;

    @Builder
    public UpdateProductRequestDto(Long id, String name, int price, int stock, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isDeleted = isDeleted;
    }

    public Product toProduct() {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .stock(stock)
                .isDeleted(isDeleted)
                .build();
    }
}
