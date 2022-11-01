package com.flab.marketgola.order.mapper;

import com.flab.marketgola.order.domain.OrderProduct;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderProductMapper {

    Long insert(OrderProduct orderProduct);

    Optional<OrderProduct> findById(Long id);

    void deleteAll();
}
