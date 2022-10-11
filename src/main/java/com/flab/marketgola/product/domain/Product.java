package com.flab.marketgola.product.domain;


import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product {

    private Long id;
    private String name;
    private int price;
    private int stock;
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DisplayProduct displayProduct;

    @Builder
    public Product(Long id, String name, int price, int stock, boolean isDeleted,
            DisplayProduct displayProduct) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.displayProduct = displayProduct;
        this.isDeleted = isDeleted;
    }
}
