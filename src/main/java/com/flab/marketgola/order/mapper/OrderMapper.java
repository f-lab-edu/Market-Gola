package com.flab.marketgola.order.mapper;

import com.flab.marketgola.order.domain.Order;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMapper {

    Long insert(Order order);

    Optional<Order> findById(Long id);
}
