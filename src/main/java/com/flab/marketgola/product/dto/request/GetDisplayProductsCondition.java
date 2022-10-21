package com.flab.marketgola.product.dto.request;

import com.flab.marketgola.product.domain.SortType;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetDisplayProductsCondition {

    private SortType sortType = SortType.PRICE_ASC;
    @Positive
    private int page = 1;
    @Positive
    private int perPage = 50;
    private int offSet;

    @Builder
    public GetDisplayProductsCondition(SortType sortType, int page, int perPage) {
        this.sortType = sortType;
        this.page = page;
        this.perPage = perPage;
    }

    public int getOffset() {
        return (page - 1) * perPage;
    }
}
