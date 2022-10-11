package com.flab.marketgola.product.mapper;

import com.flab.marketgola.product.domain.DisplayProduct;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DisplayProductMapper {

    void insert(DisplayProduct displayProduct);

    Optional<DisplayProduct> findById(Long id);

    void update(DisplayProduct displayProduct);
}
