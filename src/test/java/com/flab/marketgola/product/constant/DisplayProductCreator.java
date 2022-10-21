package com.flab.marketgola.product.constant;

import com.flab.marketgola.product.dto.request.CreateDisplayProductRequestDto;
import com.flab.marketgola.product.dto.request.CreateProductRequestDto;
import com.flab.marketgola.product.mapper.DisplayProductMapper;
import com.flab.marketgola.product.mapper.ProductMapper;
import com.flab.marketgola.product.service.ProductService;

public class DisplayProductCreator {

    private DisplayProductMapper displayProductMapper;
    private ProductMapper productMapper;

    private String name = TestDisplayProductFactory.DISPLAY_PRODUCT_NAME;
    private String descriptionUrl = TestDisplayProductFactory.DESCRIPTION_IMAGE_URL;
    private String mainImageUrl = TestDisplayProductFactory.MAIN_IMAGE_URL;
    private int price = TestProductFactory.PRICE;
    private int categoryId = TestDisplayProductFactory.CATEGORY_ID;

    public DisplayProductCreator(DisplayProductMapper displayProductMapper,
            ProductMapper productMapper) {
        this.displayProductMapper = displayProductMapper;
        this.productMapper = productMapper;
    }

    private ProductService productService;

    public DisplayProductCreator(ProductService productService) {
        this.productService = productService;
    }

    public DisplayProductCreator name(String name) {
        this.name = name;
        return this;
    }

    public DisplayProductCreator descriptionImageName(String descriptionImageName) {
        this.descriptionUrl = descriptionImageName;
        return this;
    }

    public DisplayProductCreator mainImageName(String mainImageName) {
        this.mainImageUrl = mainImageName;
        return this;
    }

    public DisplayProductCreator price(int price) {
        this.price = price;
        return this;
    }

    public DisplayProductCreator categoryID(int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public void create() {
        CreateProductRequestDto createProductRequest = TestProductFactory.generalCreateRequest()
                .price(price)
                .build();

        CreateDisplayProductRequestDto createDisplayProductRequest = TestDisplayProductFactory.generalCreateRequest()
                .name(name)
                .mainImageUrl(mainImageUrl)
                .descriptionImageUrl(descriptionUrl)
                .product(createProductRequest)
                .productCategoryId(categoryId)
                .build();

        productService.createDisplayProductWithProducts(createDisplayProductRequest);
    }
}
