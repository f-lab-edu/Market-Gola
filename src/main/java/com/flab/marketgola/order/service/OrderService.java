package com.flab.marketgola.order.service;

import com.flab.marketgola.order.domain.Order;
import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.order.dto.request.CreateOrderRequestDto;
import com.flab.marketgola.order.dto.response.OrderResponseDto;
import com.flab.marketgola.order.exception.NoSuchOrderException;
import com.flab.marketgola.order.mapper.OrderMapper;
import com.flab.marketgola.order.mapper.OrderProductMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderRepository;
    private final OrderProductMapper orderProductRepository;
    private final StockSubtractionStrategy stockSubtractionStrategy;

    @Transactional
    public long createOrder(long userId, CreateOrderRequestDto request) {
        Order order = request.toOrder(userId);
        List<OrderProduct> orderProducts = request.toOrderProducts();

        stockSubtractionStrategy.subtractStock(orderProducts);

        orderRepository.insert(order);

        orderProducts.forEach(orderProduct -> {
            orderProduct.setOrder(order);
            orderProductRepository.insert(orderProduct);
        });

        return order.getId();
    }

    public OrderResponseDto getOrderById(long id) {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchOrderException::new);
        return OrderResponseDto.of(order);
    }
}
