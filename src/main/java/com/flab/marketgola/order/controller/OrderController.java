package com.flab.marketgola.order.controller;

import com.flab.marketgola.order.dto.request.CreateOrderRequestDto;
import com.flab.marketgola.order.service.OrderService;
import com.flab.marketgola.user.controller.argumentresolver.AuthenticatedUser;
import com.flab.marketgola.user.domain.LoginUser;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(OrderController.BASE_PATH)
@RestController
public class OrderController {

    public static final String BASE_PATH = "/orders";

    private final OrderService orderService;

    @PostMapping
    ResponseEntity<Map<String, Long>> createOrder(
            @RequestBody @Validated CreateOrderRequestDto requestDto,
            @AuthenticatedUser LoginUser loginUser) {

        long orderId = orderService.createOrder(loginUser.getId(), requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(BASE_PATH + "/" + orderId));

        Map<String, Long> response = Map.of("orderId", orderId);

        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }
}
