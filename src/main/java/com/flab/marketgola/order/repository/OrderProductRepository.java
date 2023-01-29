package com.flab.marketgola.order.repository;

import com.flab.marketgola.order.domain.OrderProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Query("select op from OrderProduct op join op.order o where o.id=:id")
    List<OrderProduct> findByOrderId(@Param("id") Long id);

}
