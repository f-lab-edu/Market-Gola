package com.flab.marketgola.order.service;

import com.flab.marketgola.order.domain.OrderProduct;
import java.util.List;

public interface StockSubtractionStrategy {

    void subtractStock(List<OrderProduct> orderProducts);
}
