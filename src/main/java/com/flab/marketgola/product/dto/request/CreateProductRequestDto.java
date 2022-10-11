package com.flab.marketgola.product.dto.request;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductRequestDto {
    @NotBlank(message = "상품의 이름을 입력하지 않았습니다.")
    @Size(min = 1, max = 100, message = "상품의 이름은 1자 이상 100자 이하로 입력해주세요.")
    private String name;
    @Min(value = 100, message = "상품의 가격은 100원 이상이어야 합니다.")
    private int price;
    @Min(value = 0, message = "상품의 재고는 0개 이상이어야 합니다.")
    private int stock;
    private Long displayProductId;

    @Builder
    public CreateProductRequestDto(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    //toEntity
    public Product toProduct() {
        return Product.builder()
                .name(name)
                .price(price)
                .stock(stock)
                .displayProduct(DisplayProduct.builder().id(displayProductId).build())
                .build();
    }
}
