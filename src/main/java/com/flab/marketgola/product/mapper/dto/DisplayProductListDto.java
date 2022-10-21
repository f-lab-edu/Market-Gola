package com.flab.marketgola.product.mapper.dto;

import com.flab.marketgola.product.domain.DisplayProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class DisplayProductListDto {

    long total = 0;
    List<DisplayProduct> displayProducts = new ArrayList<>();
}
