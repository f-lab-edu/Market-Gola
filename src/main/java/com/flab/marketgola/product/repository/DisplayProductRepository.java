package com.flab.marketgola.product.repository;

import com.flab.marketgola.product.domain.DisplayProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DisplayProductRepository extends JpaRepository<DisplayProduct, Long>,
        DisplayProductRepositoryCustom {

    @Query("select dp from DisplayProduct dp join dp.products p where dp.id = :id and p.isDeleted = false")
    Optional<DisplayProduct> findById(@Param("id") Long id);
}
