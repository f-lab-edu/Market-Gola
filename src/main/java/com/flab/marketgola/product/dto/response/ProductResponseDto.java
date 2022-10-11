package com.flab.marketgola.product.dto.response;

import com.flab.marketgola.product.domain.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private int stock;
    private boolean isDeleted;


    @Builder
    public ProductResponseDto(Long id, String name, int price, int stock, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isDeleted = isDeleted;
    }

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .isDeleted(product.isDeleted())
                .build();
    }
}
