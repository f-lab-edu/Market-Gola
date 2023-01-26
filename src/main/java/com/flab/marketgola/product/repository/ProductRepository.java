package com.flab.marketgola.product.repository;

import com.flab.marketgola.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
