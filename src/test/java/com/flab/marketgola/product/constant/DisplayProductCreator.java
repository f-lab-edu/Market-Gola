package com.flab.marketgola.product.constant;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.domain.ProductCategory;
import com.flab.marketgola.product.repository.DisplayProductRepository;
import com.flab.marketgola.product.repository.ProductRepository;
import com.flab.marketgola.product.service.ProductService;

public class DisplayProductCreator {

    private DisplayProductRepository displayProductRepository;
    private ProductRepository productRepository;

    private String name = TestDisplayProductFactory.DISPLAY_PRODUCT_NAME;

    private String mainImageName = TestDisplayProductFactory.MAIN_IMAGE_NAME;

    private String descriptionImageName = TestDisplayProductFactory.DESCRIPTION_IMAGE_NAME;
    private String descriptionUrl = TestDisplayProductFactory.DESCRIPTION_IMAGE_URL;
    private String mainImageUrl = TestDisplayProductFactory.MAIN_IMAGE_URL;
    private int price = TestProductFactory.PRICE;
    private int categoryId = TestDisplayProductFactory.CATEGORY_ID;

    public DisplayProductCreator(DisplayProductRepository displayProductRepository,
            ProductRepository productRepository) {
        this.displayProductRepository = displayProductRepository;
        this.productRepository = productRepository;
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
        DisplayProduct displayProduct = TestDisplayProductFactory.generalDisplayProduct()
                .name(name)
                .price(price)
                .mainImageName(mainImageName)
                .descriptionImageName(descriptionImageName)
                .category(ProductCategory.builder().id(categoryId).build())
                .build();

        displayProductRepository.save(displayProduct);

        Product product = TestProductFactory.generalProduct()
                .price(price)
                .displayProduct(displayProduct)
                .build();

        productRepository.save(product);
    }
}
