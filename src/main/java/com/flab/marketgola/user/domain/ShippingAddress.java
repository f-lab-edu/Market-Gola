package com.flab.marketgola.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String address;
    @Column(nullable = false)
    private boolean isDefault;

    @Builder
    public ShippingAddress(String address, boolean isDefault) {
        this.address = address;
        this.isDefault = isDefault;
    }
}
