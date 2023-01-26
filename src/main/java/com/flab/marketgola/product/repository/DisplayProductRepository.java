package com.flab.marketgola.product.repository;

import com.flab.marketgola.product.domain.DisplayProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisplayProductRepository extends JpaRepository<DisplayProduct, Long>,
        DisplayProductRepositoryCustom {

}
