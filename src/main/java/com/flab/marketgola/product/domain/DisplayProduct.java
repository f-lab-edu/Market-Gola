package com.flab.marketgola.product.domain;

import com.flab.marketgola.image.domain.DescriptionImage;
import com.flab.marketgola.image.domain.MainImage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DisplayProduct {

    private Long id;
    private String name;
    private String descriptionImageName;
    private String mainImageName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ProductCategory category;
    private List<Product> products;

    @Builder
    public DisplayProduct(Long id, String name, String descriptionImageName, String mainImageName,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            ProductCategory category, List<Product> products) {
        this.id = id;
        this.name = name;
        this.descriptionImageName = descriptionImageName;
        this.mainImageName = mainImageName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.category = category;
        this.products = products;
    }

    public int setPrice() {
        return products.stream()
                .mapToInt(Product::getPrice)
                .min()
                .orElse(0);
    }

    public String getDescriptionImageUrl() {
        return DescriptionImage.generateUrl(descriptionImageName);
    }

    public String getMainImageUrl() {
        return MainImage.generateUrl(mainImageName);
    }
}
