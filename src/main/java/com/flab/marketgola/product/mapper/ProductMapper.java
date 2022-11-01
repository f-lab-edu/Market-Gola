package com.flab.marketgola.product.mapper;

import com.flab.marketgola.product.domain.Product;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProductMapper {

    void insert(Product product);

    Optional<Product> findById(Long id);

    int updateStatusByDisplayProductId(@Param("id") Long id, @Param("product") Product product);

    void insertOrUpdate(List<Product> products);

    int updateStockOptimistic(Product product);

    void deleteAll();
}
