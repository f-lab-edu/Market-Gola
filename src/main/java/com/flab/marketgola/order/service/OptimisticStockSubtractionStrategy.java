package com.flab.marketgola.order.service;

import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.order.exception.OutOfStockException;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.exception.NoSuchProductException;
import com.flab.marketgola.product.mapper.ProductMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OptimisticStockSubtractionStrategy implements StockSubtractionStrategy {

    public static final int WAITING_TIME = 50;
    private final ProductMapper productMapper;

    @Override
    public void subtractStock(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            subtractStockUntilSuccess(orderProduct.getProduct().getId(), orderProduct.getCount());
        }
    }

    private void subtractStockUntilSuccess(long productId, int count) {
        int updatedCount = 0;
        while (updatedCount == 0) {
            Product findProduct = productMapper.findById(productId)
                    .orElseThrow(NoSuchProductException::new);

            checkStock(count, findProduct);

            findProduct.subtractStock(count);
            updatedCount = productMapper.updateStockOptimistic(findProduct);

            waitIfFailed(updatedCount);
        }
    }

    private void checkStock(int count, Product findProduct) {
        int stock = findProduct.getStock();

        if (count > stock) {
            throw new OutOfStockException();
        }
    }

    private void waitIfFailed(int updatedCount) {
        if (updatedCount > 0) {
            return;
        }

        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
