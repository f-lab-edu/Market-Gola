package com.flab.marketgola.product.mapper;

import com.flab.marketgola.product.domain.DisplayProduct;
import com.flab.marketgola.product.dto.request.GetDisplayProductsCondition;
import com.flab.marketgola.product.mapper.dto.DisplayProductListDto;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DisplayProductMapper {

    void insert(DisplayProduct displayProduct);

    Optional<DisplayProduct> findById(Long id);

    void update(DisplayProduct displayProduct);

    Optional<DisplayProductListDto> findByCategoryId(@Param("categoryId") int categoryId,
            @Param("condition") GetDisplayProductsCondition condition);

    void deleteAll();
}
