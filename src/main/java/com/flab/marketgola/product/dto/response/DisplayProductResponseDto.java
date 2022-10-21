package com.flab.marketgola.product.dto.response;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.domain.ProductCategory;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DisplayProductResponseDto {

    private Long id;
    private String name;
    private int price;
    private String descriptionImageWebUrl;
    private String mainImageWebUrl;
    private ProductCategory category;
    private List<ProductResponseDto> products;

    @Builder
    public DisplayProductResponseDto(Long id, String name, int price,
            String descriptionImageWebUrl,
            String mainImageWebUrl, ProductCategory category, List<ProductResponseDto> products) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.descriptionImageWebUrl = descriptionImageWebUrl;
        this.mainImageWebUrl = mainImageWebUrl;
        this.category = category;
        this.products = products;
    }

    public static DisplayProductResponseDto of(DisplayProduct displayProduct) {
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        displayProduct.getProducts().forEach(product -> productResponseDtos.add(ProductResponseDto.of(product)));

        return DisplayProductResponseDto.builder()
                .id(displayProduct.getId())
                .name(displayProduct.getName())
                .price(displayProduct.getPrice())
                .descriptionImageWebUrl(displayProduct.getDescriptionImageUrl())
                .mainImageWebUrl(displayProduct.getMainImageUrl())
                .category(displayProduct.getCategory())
                .products(productResponseDtos)
                .build();
    }
}

