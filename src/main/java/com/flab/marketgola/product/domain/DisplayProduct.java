package com.flab.marketgola.product.domain;

import com.flab.marketgola.common.domain.BaseEntity;
import com.flab.marketgola.image.domain.DescriptionImage;
import com.flab.marketgola.image.domain.MainImage;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class DisplayProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 300, nullable = false)
    private String descriptionImageName;

    @Column(length = 300, nullable = false)
    private String mainImageName;

    @Transient
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory category;

    @OneToMany(mappedBy = "displayProduct")
    private List<Product> products;

    @Builder
    public DisplayProduct(Long id, String name, String descriptionImageName, String mainImageName,
            ProductCategory category, List<Product> products) {
        this.id = id;
        this.name = name;
        this.descriptionImageName = descriptionImageName;
        this.mainImageName = mainImageName;
        this.category = category;
        this.products = products;
    }

    public int getPrice() {
        if (price == 0) {
            return products.stream()
                    .mapToInt(Product::getPrice)
                    .min()
                    .orElse(0);
        }

        return price;
    }

    public String getDescriptionImageUrl() {
        return DescriptionImage.generateUrl(descriptionImageName);
    }

    public String getMainImageUrl() {
        return MainImage.generateUrl(mainImageName);
    }
}
