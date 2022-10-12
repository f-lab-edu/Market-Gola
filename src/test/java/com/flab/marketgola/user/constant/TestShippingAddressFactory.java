package com.flab.marketgola.user.constant;

import com.flab.marketgola.user.domain.ShippingAddress;

public class TestShippingAddressFactory {

    public static final String ADDRESS = TestUserFactory.ADDRESS;
    public static final boolean IS_DEFAULT = true;

    public static ShippingAddress.ShippingAddressBuilder generalShippingAddress() {
        return ShippingAddress.builder()
                .address(ADDRESS)
                .isDefault(IS_DEFAULT)
                .user(TestUserFactory.generalUser().build());
    }

}
