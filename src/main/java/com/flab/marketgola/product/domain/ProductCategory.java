package com.flab.marketgola.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCategory {

    private int id;
    private String name;
    private ProductCategory parentCategory;

    @Builder
    public ProductCategory(int id, String name, ProductCategory parentCategory) {
        this.id = id;
        this.name = name;
        this.parentCategory = parentCategory;
    }
}
