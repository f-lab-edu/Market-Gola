package com.flab.marketgola.product.repository;

import com.flab.marketgola.product.domain.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Product p set p.isDeleted=:isDeleted where p.displayProduct.id=:id")
    int updateStateByDisplayProductId(@Param("isDeleted") boolean isDeleted,
            @Param("id") Long displayProductId);

    @Query("select p from Product p where p.id = :id and p.isDeleted = false")
    Optional<Product> findById(@Param("id") Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Product p set p.stock=:stock where p.id=:#{#product.id} and p.updatedAt=:#{#product.updatedAt}")
    int updateStockOptimistic(@Param("stock") int updatedStock, @Param("product") Product product);
}
