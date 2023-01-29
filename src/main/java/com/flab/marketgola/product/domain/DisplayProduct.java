package com.flab.marketgola.product.domain;

import com.flab.marketgola.common.domain.BaseEntity;
import com.flab.marketgola.image.domain.DescriptionImage;
import com.flab.marketgola.image.domain.MainImage;
import java.util.ArrayList;
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

    @Column(nullable = false)
    private int price;

    @Column(length = 300, nullable = false)
    private String descriptionImageName;

    @Column(length = 300, nullable = false)
    private String mainImageName;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    private ProductCategory category;

    @OneToMany(mappedBy = "displayProduct")
    private List<Product> products = new ArrayList<>();

    @Builder
    public DisplayProduct(Long id, String name, int price, String descriptionImageName,
            String mainImageName, ProductCategory category, List<Product> products) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.descriptionImageName = descriptionImageName;
        this.mainImageName = mainImageName;
        this.category = category;
        this.products = products;
    }

    public String getDescriptionImageUrl() {
        return DescriptionImage.generateUrl(descriptionImageName);
    }

    public String getMainImageUrl() {
        return MainImage.generateUrl(mainImageName);
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeCategory(ProductCategory category) {
        this.category = category;
    }

    public void calculatePrice(List<Product> products) {
        this.price = products.stream().mapToInt(Product::getPrice).min().getAsInt();
    }

    public void update(DisplayProduct updateDisplayProduct) {
        this.name = updateDisplayProduct.name;
        this.price = updateDisplayProduct.price;
        this.descriptionImageName = updateDisplayProduct.descriptionImageName;
        this.mainImageName = updateDisplayProduct.mainImageName;
        this.category = updateDisplayProduct.category;
        this.products = updateDisplayProduct.products;
    }

    //Builder로 생성되더라도 products는 초기화되어 있도록 추가, 아래 빌더는 @Builder로 만들어진 빌더와 합쳐진다
    public static class DisplayProductBuilder {

        private List<Product> products = new ArrayList<>();
    }
}
