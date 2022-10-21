package com.flab.marketgola.product.constant;

import com.flab.marketgola.product.domain.SortType;
import com.flab.marketgola.product.dto.request.GetDisplayProductsCondition;

public class TestDisplayProductListFactory {

    public static final SortType SORT_TYPE = SortType.PRICE_ASC;
    public static final int PAGE = 1;
    public static final int PER_PAGE = 50;


    public static GetDisplayProductsCondition.GetDisplayProductsConditionBuilder generalCondition() {
        return GetDisplayProductsCondition.builder()
                .sortType(SORT_TYPE)
                .page(PAGE)
                .perPage(PER_PAGE);
    }
}
