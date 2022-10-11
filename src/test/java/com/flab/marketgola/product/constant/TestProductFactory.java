package com.flab.marketgola.product.constant;

import com.flab.marketgola.product.dto.request.CreateProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateProductRequestDto;
import java.util.List;

public class TestProductFactory {
    public static final long PRE_INSERTED_PRODUCT_ID_1 = 1L;
    public static final long PRE_INSERTED_PRODUCT_ID_2 = 2L;
    public static final String PRODUCT_NAME = "친환경 손질 유러피안 믹스";
    public static final int PRICE = 1000;
    public static final int STOCK = 10;
    public static final boolean IS_DELETED = false;

    public static CreateProductRequestDto generateCreateProductRequestDto() {
        return CreateProductRequestDto.builder()
                .name(PRODUCT_NAME)
                .price(PRICE)
                .stock(STOCK)
                .build();
    }

}
