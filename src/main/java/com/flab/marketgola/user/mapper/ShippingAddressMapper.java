package com.flab.marketgola.user.mapper;

import com.flab.marketgola.user.domain.ShippingAddress;
import com.flab.marketgola.user.domain.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ShippingAddressMapper {

    void insert(ShippingAddress shippingAddress);

    List<ShippingAddress> findAllByUser(User user);
}
