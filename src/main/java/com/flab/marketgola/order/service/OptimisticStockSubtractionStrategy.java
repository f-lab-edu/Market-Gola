package com.flab.marketgola.order.service;

import com.flab.marketgola.order.domain.OrderProduct;
import com.flab.marketgola.order.exception.OutOfStockException;
import com.flab.marketgola.product.domain.Product;
import com.flab.marketgola.product.exception.NoSuchProductException;
import com.flab.marketgola.product.repository.ProductRepository;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OptimisticStockSubtractionStrategy implements StockSubtractionStrategy {

    public static final int WAITING_TIME = 50;
    private final ProductRepository productRepository;
    private final EntityManager em;

    @Override
    public void subtractStock(List<OrderProduct> orderProducts) {
        for (OrderProduct orderProduct : orderProducts) {
            subtractStockUntilSuccess(orderProduct);
        }
    }

    private void subtractStockUntilSuccess(OrderProduct orderProduct) {
        Long productId = orderProduct.getProduct().getId();
        int count = orderProduct.getCount();

        int updatedRowCount = 0;
        while (updatedRowCount == 0) {
            Product findProduct = productRepository.findById(productId)
                    .orElseThrow(NoSuchProductException::new);

            orderProduct.setProduct(findProduct);

            checkStock(count, findProduct);

            int updatedStockCount = findProduct.getStock() - count;

            // JPA @Version을 이용한 Lock 방식의 경우 saveAndFlush를 사용하여 쿼리를 날려야 하는데,이 때 ObjectOptimisticLockingFailureException가 뜬다.
            // 이 예외를 잡아서 처리해주더라도 이미 전체 트랜잭션은 Rollback 대상으로 찍혀서 제대로 동작하지 않는다. 따라서 JPQL을 통해 낙관적 락을 직접 구현한다.
            // findProduct의 재고를 직접 수정할 경우 아래의 JPQL 쿼리와 함께 변경감지에 의한 추가 쿼리가 나가므로 업데이트 될 재고를 직접 넘긴다.
            updatedRowCount = productRepository.updateStockOptimistic(updatedStockCount,
                    findProduct);

            waitIfFailed(updatedRowCount);
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
