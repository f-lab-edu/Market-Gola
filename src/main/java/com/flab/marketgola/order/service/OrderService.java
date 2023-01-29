package com.flab.marketgola.order.service;

import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.order.dto.request.CreateOrderRequestDto;
import com.flab.marketgola.order.dto.response.OrderResponseDto;
import com.flab.marketgola.order.repository.OrderProductRepository;
import com.flab.marketgola.order.repository.OrderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final StockSubtractionStrategy stockSubtractionStrategy;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long createOrder(long userId, CreateOrderRequestDto request) {
        Order order = request.toOrder(userId);
        List<OrderProduct> orderProducts = request.toOrderProducts();

        stockSubtractionStrategy.subtractStock(orderProducts);

        orderRepository.save(order);

        orderProducts.forEach(orderProduct -> orderProduct.setOrder(order));

        orderProductRepository.saveAll(orderProducts);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(long id) {
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(id);
        return OrderResponseDto.of(orderProducts);
    }
}
