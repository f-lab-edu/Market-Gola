package com.flab.marketgola.product.domain;


import com.flab.marketgola.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 256, nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "display_product_id", nullable = false)
    private DisplayProduct displayProduct;

    @Builder
    public Product(Long id, String name, int price, int stock, boolean isDeleted,
            DisplayProduct displayProduct) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.displayProduct = displayProduct;
        this.isDeleted = isDeleted;
        if (displayProduct != null) {
            this.displayProduct.getProducts().add(this); // 양방향 연관 맺기
        }
    }

    public void subtractStock(int count) {
        this.stock -= count;
    }
}
