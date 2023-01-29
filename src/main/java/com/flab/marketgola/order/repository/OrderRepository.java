package com.flab.marketgola.order.repository;

import com.flab.marketgola.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
