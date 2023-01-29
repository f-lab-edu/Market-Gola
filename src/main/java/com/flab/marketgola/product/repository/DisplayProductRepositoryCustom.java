package com.flab.marketgola.product.repository;

import com.flab.marketgola.product.domain.DisplayProduct;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DisplayProductRepositoryCustom {

    Optional<DisplayProduct> findByIdWithAll(Long id);

    Page<DisplayProduct> findByCategory(int categoryId, Pageable pageable);

}
