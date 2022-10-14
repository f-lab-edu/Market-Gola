package com.flab.marketgola.product.constant;

import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.dto.request.CreateProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateProductRequestDto;

public class TestProductFactory {

    public static final long PRODUCT_ID = 1L;
    public static final String PRODUCT_NAME = "친환경 손질 유러피안 믹스";
    public static final int PRICE = 1000;
    public static final int STOCK = 10;
    public static final boolean IS_DELETED = false;

    public static Product.ProductBuilder generalProduct() {
        return Product.builder()
                .name(PRODUCT_NAME)
                .price(PRICE)
                .stock(STOCK)
                .isDeleted(IS_DELETED);
    }

    public static CreateProductRequestDto.CreateProductRequestDtoBuilder generalCreateRequest() {
        return CreateProductRequestDto.builder()
                .name(PRODUCT_NAME)
                .price(PRICE)
                .stock(STOCK);
    }

    public static UpdateProductRequestDto.UpdateProductRequestDtoBuilder generalUpdateRequest() {
        return UpdateProductRequestDto.builder()
                .id(PRODUCT_ID)
                .name(PRODUCT_NAME)
                .price(PRICE)
                .stock(STOCK)
                .isDeleted(IS_DELETED);
    }
}
