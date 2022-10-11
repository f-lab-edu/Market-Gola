package com.flab.marketgola.product.constant;

import com.flab.marketgola.product.domain.ProductCategory;
import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.CreateProductRequestDto;
import com.flab.marketgola.product.dto.request.UpdateDisplayProductWithProductsRequestDto;
import com.flab.marketgola.product.dto.request.UpdateProductRequestDto;
import java.util.List;

public class TestDisplayProductFactory {

    public static final long PRE_INSERTED_DISPLAY_PRODUCT_ID = 1L;

    public static final String DISPLAY_PRODUCT_NAME = "친환경 손질 유러피안 샐러드 6종";
    public static final String DESCRIPTION_IMAGE_NAME = "e662963a-42dc-11ed-b878-0242ac120002.jpg";
    public static final String MAIN_IMAGE_NAME = "eed16cce-42dc-11ed-b878-0242ac120002.jpg";
    public static final ProductCategory CATEGORY = new ProductCategory(1, "과일", null);
    public static final int CATEGORY_ID = 1;

    public static CreateDisplayProductRequestDto generateCreateDisplayProductRequestDto(
            List<CreateProductRequestDto> createProductRequestDtos) {

        return CreateDisplayProductRequestDto.builder()
                .name(DISPLAY_PRODUCT_NAME)
                .mainImageName(MAIN_IMAGE_NAME)
                .descriptionImageName(DESCRIPTION_IMAGE_NAME)
                .productCategoryId(CATEGORY_ID)
                .products(createProductRequestDtos)
                .build();
    }

    public static UpdateDisplayProductWithProductsRequestDto generateUpdateDisplayProductRequestDto(
            List<UpdateProductRequestDto> productRequestDto) {
        return UpdateDisplayProductWithProductsRequestDto.builder()
                .id(PRE_INSERTED_DISPLAY_PRODUCT_ID)
                .name(DISPLAY_PRODUCT_NAME)
                .mainImageName(MAIN_IMAGE_NAME)
                .descriptionImageName(DESCRIPTION_IMAGE_NAME)
                .productCategoryId(CATEGORY_ID)
                .products(productRequestDto)
                .build();
    }

}
