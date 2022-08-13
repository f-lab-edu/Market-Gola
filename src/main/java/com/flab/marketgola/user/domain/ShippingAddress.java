package com.flab.marketgola.user.domain;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class ShippingAddress {

    private Long id;
    private User user;
    private String address;
    private boolean isDefault;

    @Builder
    public ShippingAddress(User user, String address, boolean isDefault) {
        this.user = user;
        this.address = address;
        this.isDefault = isDefault;
    }
}
